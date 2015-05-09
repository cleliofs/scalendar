package models.com.codesynergy.domain

import main.scala.com.codesynergy.domain.User
import play.api.libs.json.Json

/**
 * Created by clelio on 09/05/15.
 */
case class Calendar(name: String, users: Set[User] = Set())

object Calendar {
  implicit val calendarFormat = Json.format[Calendar]
}
