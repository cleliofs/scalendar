package main.scala.com.codesynergy.domain

import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.json.{Json, Writes}

/**
 * Created by clelio on 19/04/15.
 */
case class Event(var startDate: Date, var endDate: Date, var title: String = "") {
  override def toString: String = {
    val formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val formattedStartDate = formatter.format(startDate)
    val formattedEndDate = formatter.format(endDate)
    s"Event: $title (Start Date: $formattedStartDate - End Date: $formattedEndDate)"
  }

  implicit val eventWrites = new Writes[Event] {
    def writes(event: Event) = Json.obj(
      "startDate" -> event.startDate,
      "endDate" -> event.endDate,
      "title" -> event.title
    )
  }

}
