package test.scala.com.codesynergy.domain

import java.util.Calendar

import main.scala.com.codesynergy.domain.Event
import org.scalatest.FlatSpec

import scala.collection.mutable

/**
 * Created by clelio on 19/04/15.
 */
class EventTest extends FlatSpec {

  "An Event" should "have a start and end date" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 17, 30)
    val startDate = c.getTime
    c.set(2015, 4, 19, 18, 30)
    val endDate = c.getTime
    val e = Event(startDate, endDate, "New Event")
    assert(startDate.equals(e.startDate))
    assert(endDate.equals(e.endDate))
    assert("New Event"==e.title)
  }

  "An Event" should "print nicely to the output" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 17, 30, 0)
    val startDate = c.getTime
    c.set(2015, 4, 19, 18, 30, 0)
    val endDate = c.getTime
    val title = "This is a new event";
    val e = Event(startDate, endDate, title);

    val s: String = s"Event: $title (Start Date: 19/05/2015 17:30:00 - End Date: 19/05/2015 18:30:00)";
    assert(s==e.toString)
    print(e)
  }

}
