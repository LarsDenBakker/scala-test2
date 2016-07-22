package com.larscode.testscala2.person

import java.util.UUID

import com.larscode.testscala2.repository.{EntityHandler, withBSONHandling, withValidation}
import reactivemongo.bson._
import com.wix.accord.dsl._
import com.wix.accord.transform.ValidationTransform.TransformedValidator

case class Person(name: String, age: Int) {

}

object Person extends EntityHandler[Person] with withBSONHandling[Person] with withValidation[Person] {

  override implicit var bsonHandler = Macros.handler[Person]

  override implicit var entityValidator = validator[Person] { p =>
    //    p._id is valid
    p.name has (size within (0 to 16))
    p.age is within(0 to 16)
  }
}