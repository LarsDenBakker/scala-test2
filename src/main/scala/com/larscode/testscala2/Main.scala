package com.larscode.testscala2

import akka.actor.Props
import com.larscode.testscala2.person.Person
import com.larscode.testscala2.repository.MongoRepository
import reactivemongo.api.{DefaultDB, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, _}

import scala.concurrent.{Await, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object Main {

  val COLLECTION_USERS = "users"
  val COLLECTION_CHARACTERS = "characters"

  def main(args: Array[String]): Unit = {
    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))
    val db = connection.database("test8")
    val pc = db.map(_.collection[BSONCollection]("persons"))
    val finished = Promise[Unit]
    pc.map(pc => {
      driver.system.actorOf(Props(new MongoRepository[Person](pc)), "personRepository")
      driver.system.actorOf(Props(new CommandRunner(finished)))
    })

    Await.result(finished.future, 1 minute)
  }

}
