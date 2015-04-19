package test.scala.com.codesynergy.service

import java.util.Calendar

import main.scala.com.codesynergy.domain.{Event, User}
import main.scala.com.codesynergy.service.CalendarService
import org.scalatest.FlatSpec


/**
 * Created by clelio on 19/04/15.
 */
class CalendarServiceTest extends FlatSpec {

  "A CalendarService" should "add a user" in {
    val u1 = User("cleliofs")
    val u2 = User("newuser")

    val c = new CalendarService;
    c.save(u1)
    assert(User("cleliofs")==c.find(u1).get)
    c.save(u2)
    assert(User("newuser")==c.find(u2).get)
    assert(Nil==c.find(User("otheruser")).getOrElse(Nil))
  }

  "A CalendarService" should "show all registered users" in {
    val u1 = User("cleliofs")
    val u2 = User("newuser")

    val c = new CalendarService
    c.save(u1)
    c.save(u2)
    c.showUsers;
  }

  "A CalendarService" should "add an Event to a user" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 17, 30)
    val startDate = c.getTime
    c.set(2015, 4, 19, 18, 30)
    val endDate = c.getTime
    val e1 = Event(startDate, endDate, "New Event 1")
    val e2 = Event(startDate, endDate, "New Event 2")

    val calendar = new CalendarService
    calendar.save(User("cleliofs"))
    calendar.addEvent(User("cleliofs"), e1)
    calendar.addEvent(User("cleliofs"), e2)
    assert(2==calendar.events.size)
  }

}
