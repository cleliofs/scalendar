package models.com.codesynergy.domain

import java.sql.Date
import java.text.SimpleDateFormat

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}
import slick.driver.H2Driver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}


/**
 * Created by clelio on 10/05/15.
 */
case class Calendar(name: String, id: Option[Int] = None) {
  implicit val calendarFormat = Json.format[Calendar]

}

class CalendarTable(tag: Tag) extends Table[Calendar](tag, "CALENDAR") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")

  override def * = (name, id.?) <> (Calendar.tupled, Calendar.unapply)
}

object CalendarTable {
  lazy val calendar = TableQuery[CalendarTable]
}


case class User(
   username: String,
   name: String,
   surname: String,
   email: String = "",
   company: String = "",
   id: Option[Int] = None,
   eventId: Option[Int] = None) extends Ordered[User] {

  implicit val userFormat = Json.format[User]

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "username" -> user.username,
      "name" -> user.name,
      "surname" -> user.surname,
      "email" -> user.email,
      "company" -> user.company,
      "id" -> user.id,
      "eventId" -> user.eventId
    )
  }

  implicit  val userReads: Reads[User] = (
    (JsPath \ "username").read[String] and
    (JsPath \ "name").read[String] and
    (JsPath \ "surname").read[String] and
    (JsPath \ "email").read[String] and
    (JsPath \ "company").read[String] and
    (JsPath \ "id").read[Option[Int]] and
    (JsPath \ "eventId").read[Option[Int]]
    )(User.apply(_, _, _ ,_ ,_ , _, _))

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

class UserTable(tag: Tag) extends Table[User](tag, "USERS"){

  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def username: Rep[String] = column[String]("USERNAME")
  def name: Rep[String] = column[String]("NAME")
  def surname: Rep[String] = column[String]("SURNAME")
  def email: Rep[String] = column[String]("EMAIL")
  def company: Rep[String] = column[String]("COMPANY")
  def eventId = column[Int]("EVENT_ID")

  override def * : ProvenShape[User] =
    (username, name, surname, email, company, id.?, eventId.?) <> (User.tupled, User.unapply)

  def event: ForeignKeyQuery[EventTable, Event] = foreignKey("EVENT_FK", eventId, EventTable.events)(_.id)

}

object UserTable {
  lazy val users = TableQuery[UserTable]

  implicit class UserExtension[C[_]](q: Query[UserTable, User, C]) {
    def withEvents = q.join(EventTable.events).on(_.eventId === _.id)
  }

}


case class Event(title: String, startDate: Date, endDate: Date, id: Option[Int] = None) {
  implicit val dateRead = Reads.jodaDateReads(pattern)

  val pattern = "dd/MM/yyyy HH:mm:ss";
  val formatter = new SimpleDateFormat(pattern)


  implicit val eventWrites = new Writes[Event] {
    def writes(event: Event) = Json.obj(
      "title" -> event.title,
      "startDate" -> event.toString,
      "endDate" -> event.toString,
      "id" -> event.id
    )
  }

  implicit  val eventReads: Reads[Event] = (
    (JsPath \ "title").read[String] and
    (JsPath \ "startDate").read[Date] and
    (JsPath \ "endDate").read[Date] and
    (JsPath \ "id").read[Option[Int]]
  )(Event.apply(_, _, _, _))

  override def toString: String = {
    s"Event: $title (Start Date: ${startDate.toString} - End Date: ${endDate.toString})"
  }

}

class EventTable(tag: Tag) extends Table[Event](tag, "EVENTS") {

  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def title: Rep[String] = column[String]("TITLE")
  def startDate: Rep[Date] = column[Date]("START_DATE")
  def endDate: Rep[Date] = column[Date]("END_DATE")

  override def * : ProvenShape[Event] =
    (title, startDate, endDate, id.?) <> (Event.tupled, Event.unapply)

}

object EventTable {
  lazy val events = TableQuery[EventTable]
}
