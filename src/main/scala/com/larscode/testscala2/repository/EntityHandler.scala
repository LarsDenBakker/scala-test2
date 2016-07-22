package com.larscode.testscala2.repository

import com.wix.accord
import com.wix.accord.transform.ValidationTransform.TransformedValidator
import reactivemongo.bson.{BSONDocument, BSONHandler}

import scala.util.{Failure, Success}

trait EntityHandler[T] extends withBSONHandling[T] with withValidation[T] {

  def of(document: BSONDocument)(): Either[Iterable[String], T] = {
    bsonHandler.readTry(document) match {
      case Failure(ex) => Left(Seq(s"${ex.getMessage}"))
      case Success(t) => {
        val errors = validate(t)
        if (errors.isEmpty) Right(t) else Left(errors.get)
      }
    }
  }

  def validate(t: T): Option[Iterable[String]] = {
    com.wix.accord.validate(t) match {
      case accord.Success => None
      case accord.Failure(vs) => Some(vs.map(v => s"${v.description.getOrElse("NO DESCRIPTION")} ${v.constraint}"))
    }
  }

  def assertValid(t: T) = {
    val errors = validate(t)
    if (errors.isDefined) throw new IllegalArgumentException(errors.get.toString)
  }
}

trait withBSONHandling[T] {
  implicit var bsonHandler: BSONHandler[BSONDocument, T]
}

trait withValidation[T] {
  implicit var entityValidator: TransformedValidator[T]
}
