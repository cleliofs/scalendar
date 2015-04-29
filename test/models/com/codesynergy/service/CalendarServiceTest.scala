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
    assert(User("cleliofs")==c.findUserByUsername(u1).get)
    c.save(u2)
    assert(User("newuser")==c.findUserByUsername(u2).get)
    assert(Nil==c.findUserByUsername(User("otheruser")).getOrElse(Nil))
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
    assert(2==calendar.events(User("cleliofs")).size)
    assert(true==calendar.events(User("cleliofs")).contains(e1))
    assert(true==calendar.events(User("cleliofs")).contains(e2))
  }

  "A CalendarService" should "check events do not clash for user" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 10, 0)
    val startDateEvent1 = c.getTime
    c.set(2015, 4, 19, 11, 0)
    val endDateEvent1 = c.getTime
    val e1 = Event(startDateEvent1, endDateEvent1, "New Event 1")

    c.set(2015, 4, 19, 11, 0)
    val startDateEvent2 = c.getTime
    c.set(2015, 4, 19, 12, 0)
    val endDateEvent2 = c.getTime
    val e2 = Event(startDateEvent2, endDateEvent2, "New Event 2")

    val calendar = new CalendarService
    calendar.save(User("cleliofs"))
    calendar.addEvent(User("cleliofs"), e1)
    calendar.addEvent(User("cleliofs"), e2)
    assert(false==calendar.checkEventsClash(User("cleliofs")))
  }

  "A CalendarService" should "check events clashed for user 1" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 10, 0)
    val startDateEvent1 = c.getTime
    c.set(2015, 4, 19, 11, 0)
    val endDateEvent1 = c.getTime
    val e1 = Event(startDateEvent1, endDateEvent1, "New Event 1")

    c.set(2015, 4, 19, 10, 30)
    val startDateEvent2 = c.getTime
    c.set(2015, 4, 19, 11, 0)
    val endDateEvent2 = c.getTime
    val e2 = Event(startDateEvent2, endDateEvent2, "New Event 2")

    val calendar = new CalendarService
    calendar.save(User("cleliofs"))
    calendar.addEvent(User("cleliofs"), e1)
    calendar.addEvent(User("cleliofs"), e2)
    assert(true==calendar.checkEventsClash(User("cleliofs")))
  }

  "A CalendarService" should "check events clashed for user 2" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 10, 0)
    val startDateEvent1 = c.getTime
    c.set(2015, 4, 19, 11, 0)
    val endDateEvent1 = c.getTime
    val e1 = Event(startDateEvent1, endDateEvent1, "New Event 1")

    c.set(2015, 4, 19, 10, 30)
    val startDateEvent2 = c.getTime
    c.set(2015, 4, 19, 10, 45)
    val endDateEvent2 = c.getTime
    val e2 = Event(startDateEvent2, endDateEvent2, "New Event 2")

    val calendar = new CalendarService
    calendar.save(User("cleliofs"))
    calendar.addEvent(User("cleliofs"), e1)
    calendar.addEvent(User("cleliofs"), e2)
    assert(true==calendar.checkEventsClash(User("cleliofs")))
  }

  "A CalendarService" should "allow users to adjust/amend events" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 10, 0)
    val startDateEvent1 = c.getTime
    c.set(2015, 4, 19, 11, 0)
    val endDateEvent1 = c.getTime
    val e1 = Event(startDateEvent1, endDateEvent1, "New Event 1")

    c.set(2015, 4, 19, 10, 30)
    val startDateEvent2 = c.getTime
    c.set(2015, 4, 19, 10, 45)
    val endDateEvent2 = c.getTime
    val e2 = Event(startDateEvent2, endDateEvent2, "New Event 2")

    val calendar = new CalendarService
    calendar.save(User("cleliofs"))
    calendar.addEvent(User("cleliofs"), e1)
    calendar.addEvent(User("cleliofs"), e2)
    assert(true==calendar.checkEventsClash(User("cleliofs")))

    c.set(2015, 4, 19, 11, 0)
    val newStartDateEvent2 = c.getTime
    c.set(2015, 4, 19, 11, 15)
    val newEndDateEvent2 = c.getTime
    val newE2 = Event(newStartDateEvent2, newEndDateEvent2, "New Event 222")
    calendar.modifyEvent(User("cleliofs"), e2.title, newE2)
    assert(false==calendar.checkEventsClash(User("cleliofs")))
  }

  "A CalendarService" should "find for event in the users' events" in {
    val c = Calendar.getInstance
    c.set(2015, 4, 19, 10, 0)
    val startDateEvent1 = c.getTime
    c.set(2015, 4, 19, 11, 0)
    val endDateEvent1 = c.getTime
    val e1 = Event(startDateEvent1, endDateEvent1, "New Event 1")

    c.set(2015, 4, 19, 10, 30)
    val startDateEvent2 = c.getTime
    c.set(2015, 4, 19, 10, 45)
    val endDateEvent2 = c.getTime
    val e2 = Event(startDateEvent2, endDateEvent2, "New Event 2")

    val calendar = new CalendarService
    calendar.save(User("cleliofs"))
    calendar.addEvent(User("cleliofs"), e1)
    calendar.addEvent(User("cleliofs"), e2)

    assert(e1==calendar.findEventByTitle(User("cleliofs"), "New Event 1"))
  }


}
