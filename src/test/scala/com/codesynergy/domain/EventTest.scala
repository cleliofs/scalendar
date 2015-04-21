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

  "A Map" should "operate over items" in {
    val m = scala.collection.mutable.Map[Integer, String]();
    m(1) = "Test1";
    assert("Test1".equals(m(1)))
    m(1) = "Another Test";
    assert("Another Test".equals(m(1)))
  }

  "A List" should "operate over items" in {
    val l = scala.collection.mutable.MutableList(1, 2, 3)
    assert(1==l(0))
    l+=4
    assert(4==l(3))

    assert(true==new mutable.MutableList[String]().isEmpty)
  }
}
