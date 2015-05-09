package main.scala.com.codesynergy.domain

import java.text.SimpleDateFormat
import java.util.Date
import org.joda.time.DateTime
import play.api.libs.functional.syntax._

import play.api.libs.json.{JsPath, Reads, Json, Writes}

/**
 * Created by clelio on 19/04/15.
 */
case class Event(startDate: DateTime, endDate: DateTime, title: String = "") {

  override def toString: String = {
    s"Event: $title (Start Date: ${startDate.toString(Event.pattern)} - End Date: ${endDate.toString(Event.pattern)})"
  }
}

object Event {

  val pattern = "dd/MM/yyyy HH:mm:ss";
  val formatter = new SimpleDateFormat(pattern)

  implicit val dateRead = Reads.jodaDateReads(pattern)

  implicit val eventWrites = new Writes[Event] {
    def writes(event: Event) = Json.obj(
      "startDate" -> event.startDate.toString(pattern),
      "endDate" -> event.endDate.toString(pattern),
      "title" -> event.title
    )
  }

  implicit  val eventReads: Reads[Event] = (
      (JsPath \ "startDate").read[DateTime] and
      (JsPath \ "endDate").read[DateTime] and
      (JsPath \ "title").read[String]
    )(Event.apply(_, _, _))

}
