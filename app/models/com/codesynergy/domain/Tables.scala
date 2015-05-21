package models.com.codesynergy.domain

import play.api.libs.json.Json
import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape


// CALENDAR table
case class Calendar(id: Int, name: String)

object Calendar {
  lazy val query = TableQuery[CalendarTable]
  implicit val calendarFormat = Json.format[Calendar]
}

class CalendarTable(tag: Tag) extends Table[Calendar](tag, "CALENDAR") {
  def id = column[Int]("ID", O.PrimaryKey)
  def name = column[String]("NAME")

  override def * = (id, name) <> ((Calendar.apply _).tupled, Calendar.unapply)

}



// USER table
case class User(
    id: Int,
    username: String,
    name: String,
    surname: String,
    email: String = "",
    company: String = "",
    calendarId: Option[Int] = None) extends Ordered[User] {

  override def toString: String = {
    s"Username: $username - Email: $email ($name $surname - $company) - Events: [TODO]"
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
  lazy val query = TableQuery[UserTable]

  def findEventsUserIsOwner(username: String) = for {
    u <- query.filter(u => u.username === username)
    e <- Event.query.filter(e => e.userOwnerId === u.id)
  } yield e

  def findEventsForUser(username: String) = for {
    u <- query.filter(_.username === username)
    ue <- UserEvent.query.filter(_.userId === u.id)
    e <- Event.query.filter(_.id === ue.eventId)
  } yield e
}

class UserTable(tag: Tag) extends Table[User](tag, "USERS"){

  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def username: Rep[String] = column[String]("USERNAME")
  def name: Rep[String] = column[String]("NAME")
  def surname: Rep[String] = column[String]("SURNAME")
  def email: Rep[String] = column[String]("EMAIL")
  def company: Rep[String] = column[String]("COMPANY")
  def calendarId: Rep[Option[Int]] = column[Option[Int]]("CALENDAR_ID")

  override def * : ProvenShape[User] =
    (id, username, name, surname, email, company, calendarId) <> ((User.apply _).tupled, User.unapply)

  def calendarFk = foreignKey("calendar_fk", calendarId, Calendar.query)(c => c.id)
  
}


// USERS_EVENTS mapping table
case class UserEvent(userId: Int, eventId: Int)

class UserEventTable(tag: Tag) extends Table[UserEvent](tag, "USERS_EVENTS") {
  def userId: Rep[Int] = column[Int]("USER_ID")
  def eventId: Rep[Int] = column[Int]("EVENT_ID")

  override def * : ProvenShape[UserEvent] = (userId, eventId) <> ((UserEvent.apply _).tupled, UserEvent.unapply)

  def userFk = foreignKey("user_fk", userId, User.query)(u => u.id)
  def eventFk = foreignKey("event_fk", eventId, Event.query)(e => e.id)
}

object UserEvent {
  lazy val query = TableQuery[UserEventTable]
}



// EVENT table
case class Event(id: Int, title: String, userOwnerId: Int) {

  override def toString: String = {
    s"Event: $title"
  }

}

object Event {
  lazy val query = TableQuery[EventTable]
  implicit val eventFormat = Json.format[Event]
}

class EventTable(tag: Tag) extends Table[Event](tag, "EVENTS") {

  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey)
  def title: Rep[String] = column[String]("TITLE")
  def userOwnerId: Rep[Int] = column[Int]("USER_OWNER_ID")

  override def * : ProvenShape[Event] =
    (id, title, userOwnerId) <> ((Event.apply _).tupled, Event.unapply)

}
