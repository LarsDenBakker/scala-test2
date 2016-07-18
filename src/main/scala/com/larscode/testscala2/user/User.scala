package com.larscode.testscala2.user

import java.util.UUID

import reactivemongo.bson.Macros
import com.larscode.testscala2.bson.Handlers.bsonUUIDFormat
import com.wix.accord.dsl._

case class Username(since: Long, username: String)

object Username {
  implicit val personHandler = Macros.handler[Username]
}

case class User(_id: UUID, usernames: List[Username]) {
  def username = if (usernames.nonEmpty) usernames.head else "Unnamed"
}

object User {
  implicit val personHandler = Macros.handler[User]
  implicit val characterValidator = validator[User] { u =>
    u._id is notNull
  }
}

