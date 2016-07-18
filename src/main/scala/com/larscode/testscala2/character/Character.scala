package com.larscode.testscala2.character

import java.util.UUID

import com.wix.accord.dsl._
import reactivemongo.bson.Macros
import com.larscode.testscala2.bson.Handlers.bsonUUIDFormat

case class CharacterId(userId: UUID, index: Int)

object CharacterId {
  implicit val personHandler = Macros.handler[CharacterId]
  implicit val characterIdValidator = validator[CharacterId] { ci =>
    ci.index is within(0 to 9)
  }
}

case class Character(_id: CharacterId, nickname: String, firstName: Option[String], lastName: Option[String])

object Character {
  implicit val personHandler = Macros.handler[Character]
  implicit val characterValidator = validator[Character] { c =>
    c._id is valid
    c.nickname has(size within(1 to 16))
    c.firstName.each has(size within(1 to 16))
    c.lastName.each has(size within(1 to 16))
  }
}