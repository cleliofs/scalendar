package main.scala.com.codesynergy.service

import main.scala.com.codesynergy.domain.{Event, User}

import scala.collection.mutable.Map
import scala.collection.mutable.MutableList

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {


  private var users = MutableList[User]()

  var events = Map[User, MutableList[Event]]()

  def find(u: User): Option[User] = {
    users.find(e => e.username == u.username)
  }

  def save(u: User): Unit = {
    users += u
  }

  def showUsers: Unit = users.foreach(println)

  def addEvent(u: User, e: Event): Unit = {
    if (!events.contains(u)) events(u) = MutableList()

    events(u) += e
  }

  def checkEventsClash(u: User) = {
    def hasClashed(e: Event, list: List[Event]): Boolean = {
      if (list == Nil) false
      else if (e.eq(list.head)) hasClashed(e, list.tail)
      else if (list.head.startDate.after(e.startDate) && list.head.startDate.before(e.endDate)) true
      else hasClashed(e, list.tail)
    }
    events(u).count(hasClashed(_, events(u).toList))!=0
  }

}
