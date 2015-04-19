package test.scala.com.codesynergy.domain

import java.util.Calendar

import main.scala.com.codesynergy.domain.Event
import org.scalatest.FlatSpec

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
}
