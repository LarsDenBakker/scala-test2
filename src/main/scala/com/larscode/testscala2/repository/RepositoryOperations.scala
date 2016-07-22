package com.larscode.testscala2.repository

import com.wix.accord.transform.ValidationTransform.TransformedValidator
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONHandler}
import com.wix.accord._

import scala.concurrent.{Future, Promise}
import scala.util.Try

object RepositoryOperations {

  case class CreateOperation[T] (t: T)(implicit handler: EntityHandler[T]) {
      handler.assertValid(t)
  }

  case class CreateResponse(fut: Future[WriteResult])

  case class FindOneOperation(query: BSONDocument)

  case class FindOneResponse[T](fut: Future[Option[T]])

  case class FindOperation(query: Option[BSONDocument], limit: Option[Int])

  case class FindResponse[T](fut: Future[List[T]])

  case class DeleteOperation(query: Option[BSONDocument])

  case class DeleteResponse(obs: Future[WriteResult])


}
