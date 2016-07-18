package com.larscode.testscala2.repository

import akka.actor.Actor
import com.larscode.testscala2.repository.RepositoryOperations._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONHandler}
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson._

class MongoRepository[T](val coll: BSONCollection)
                        (implicit val personHandler: BSONDocumentReader[T] with BSONDocumentWriter[T] with BSONHandler[BSONDocument, T]) extends Actor {

  private def create(op: CreateOperation[T]) = coll.insert(op.t)

  private def findOne(op: FindOneOperation) = coll.find(op.query).one[T]
  //TODO: limit query
  private def find(op: FindOperation) = coll.find(op.query.getOrElse(BSONDocument())).cursor[T].collect[List]()

  private def delete(op: DeleteOperation) = coll.remove(op.query.getOrElse(BSONDocument()))

  override def receive = {
    case op: CreateOperation[T] => sender() ! CreateResponse(create(op))
    case op: FindOneOperation => sender() ! FindOneResponse(findOne(op))
    case op: FindOperation => sender() ! FindResponse(find(op))
    case op: DeleteOperation => sender() ! DeleteResponse(delete(op))
  }
}
