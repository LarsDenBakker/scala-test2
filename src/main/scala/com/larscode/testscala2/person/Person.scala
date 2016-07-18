package com.larscode.testscala2.person

import java.util.UUID
import com.larscode.testscala2.bson.Handlers._
import reactivemongo.bson._

case class Person(_id: UUID, name: String, age: Int)

object Person {
  implicit val personHandler = Macros.handler[Person]
}