package main.scala.com.codesynergy.domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Created by clelio on 19/04/15.
 */
case class User(
    username: String = "",
    name: String = "",
    surname: String = "",
    email: String = "",
    company: String = "",
    events: Set[Event] = Set()) extends Ordered[User] {

    override def toString: String = {
        s"Username: $username - Email: $email ($name $surname - $company) - Events: [$events]"
    }

    override def compare(that: User): Int = username.compareTo(that.username)

    override def hashCode(): Int = 41 * username.hashCode

    override def equals(other: scala.Any): Boolean = other match {
        case that: User => username.equals(that.username)
        case _ => false
    }
}

object User {
    implicit val userFormat = Json.format[User]

    implicit val userWrites = new Writes[User] {
        def writes(user: User) = Json.obj(
            "username" -> user.username,
            "name" -> user.name,
            "surname" -> user.surname,
            "email" -> user.email,
            "company" -> user.company,
            "events" -> user.events
        )
    }

    implicit  val userReads: Reads[User] = (
        (JsPath \ "username").read[String] and
        (JsPath \ "name").read[String] and
        (JsPath \ "surname").read[String] and
        (JsPath \ "email").read[String] and
        (JsPath \ "company").read[String] and
        (JsPath \ "events").read[Set[Event]]
    )(User.apply(_, _, _ ,_ ,_ , _))
}
