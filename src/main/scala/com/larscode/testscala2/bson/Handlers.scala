package com.larscode.testscala2.bson

import java.util.UUID

import reactivemongo.bson.{BSONReader, BSONString, BSONValue, BSONWriter}

object Handlers {
  implicit val bsonUUIDFormat = new BSONReader[BSONString, UUID] with BSONWriter[UUID, BSONString] {
    def write(t: UUID) = BSONString(t.toString)

    def read(bson: BSONString) = UUID.fromString(bson.value)
  }
}
