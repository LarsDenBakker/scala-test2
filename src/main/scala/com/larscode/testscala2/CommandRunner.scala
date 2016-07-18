package com.larscode.testscala2

import java.util.UUID

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.larscode.testscala2.person.Person
import com.larscode.testscala2.repository.RepositoryOperations.{FindOperation, _}
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.immutable.Queue
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

case class BlaBlo(a: String, b: String, c: String)

class CommandRunner(promise: Promise[Unit]) extends Actor {

  val personRepository = context.actorSelection("../personRepository")

  personRepository ! CreateOperation(Person(UUID.randomUUID, "a", 1))

  var commands = Queue(
    FindOneOperation(BSONDocument("name" -> "a")),
    CreateOperation(Person(UUID.randomUUID, "a", 1)),
    CreateOperation(Person(UUID.randomUUID, "b", 2)),
    CreateOperation(Person(UUID.randomUUID, "c", 3)),
    CreateOperation(Person(UUID.randomUUID, "d", 4)),
    FindOperation(None, None),
    DeleteOperation(Some(BSONDocument("name" -> "a"))),
    FindOneOperation(BSONDocument("name" -> "a")),
    FindOperation(None, None),
    DeleteOperation(Some(BSONDocument("naarewame" -> "a"))),
    FindOperation(None, Some(2)),
    FindOperation(Some(BSONDocument("rewrwer" -> "a")), None)
  )

  private def executeNext() = {
    if (commands.nonEmpty) {
      val (nextCommand, nextCommands) = commands.dequeue
      commands = nextCommands
      personRepository ! nextCommand
    } else {
      promise.complete(Success())
    }
  }

  override def receive = {
    case CreateResponse(fut) => fut.onComplete({
      case Success(x) =>
        println(s"Create success: $x")
        executeNext()

      case Failure(x) =>
        println(s"Create failed")
        x.printStackTrace()
    })

    case FindOneResponse(fut) => fut.onComplete({
      case Success(x) =>
        println(s"Find One success: $x")
        executeNext()

      case Failure(x) =>
        println(s"Find One failed")
        x.printStackTrace()
    })

    case FindResponse(fut) => fut.onComplete({
      case Success(x) =>
        println(s"Find success: $x")
        executeNext()

      case Failure(x) =>
        println(s"Find failed")
        x.printStackTrace()
    })

    case DeleteResponse(fut) => fut.onComplete({
      case Success(x) =>
        println(s"Delete success: $x")
        executeNext()

      case Failure(x) =>
        println(s"Delete failed")
        x.printStackTrace()
    })

  }

}
