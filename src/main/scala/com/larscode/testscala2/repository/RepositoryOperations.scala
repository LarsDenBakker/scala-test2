package com.larscode.testscala2.repository

import com.wix.accord.transform.ValidationTransform.TransformedValidator
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONHandler}
import com.wix.accord._

import scala.concurrent.Future

object RepositoryOperations {

  case class CreateOperation[T](t: T)

  object CreateOperation {
    def of[T](t: T)(implicit tValidator: TransformedValidator[T]): Either[CreateOperation[T], Set[Violation]] = {
      val result = validate(t)
      result match {
        case Success => Left(CreateOperation(t))
        case Failure(violations) => Right(violations)
      }
    }

    def of[T](doc: BSONDocument)(implicit personHandler: BSONDocumentReader[T] with BSONDocumentWriter[T] with BSONHandler[BSONDocument, T],
                                 tValidator: TransformedValidator[T]): Either[CreateOperation[T], Set[Violation]] = {
      val t = personHandler.read(doc)
      of(t)
    }
  }

  case class CreateResponse(fut: Future[WriteResult])

  case class FindOneOperation(query: BSONDocument)

  case class FindOneResponse[T](fut: Future[Option[T]])

  case class FindOperation(query: Option[BSONDocument], limit: Option[Int])

  case class FindResponse[T](fut: Future[List[T]])

  case class DeleteOperation(query: Option[BSONDocument])

  case class DeleteResponse(obs: Future[WriteResult])


}
