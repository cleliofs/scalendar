package main.scala.com.codesynergy.service

import main.scala.com.codesynergy.domain.{Event, User}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map, MutableList}

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {

  private var users = mutable.TreeSet[User]()

  var events = mutable.Map[User, ArrayBuffer[Event]]()

  def findUserByUsername(u: User): Option[User] = {
    users.find(e => e.username == u.username)
  }

  def save(u: User): Unit = {
    users += u
  }

  def addEventToUser(u: User, e: Event): Unit = {
    if (!users.contains(u)) save(u)
    if (!events.contains(u)) events(u) = ArrayBuffer()

    events(u) += e
    users.find(_ == u).get.addEvent(e)
  }

  def getUsers: List[User] = users.toList

  def showUsers: Unit = users.foreach(println)

  def checkEventsClash(u: User) = {
    @tailrec
    def hasClashed(e: Event, list: List[Event]): Boolean = list match {
      case Nil => false
      case h::tail if e.eq(list.head) => hasClashed(e, tail)
      case h::tail if h.startDate.toDate.after(e.startDate.toDate) && h.startDate.toDate.before(e.endDate.toDate) => true
      case h::tail => hasClashed(e, tail)
    }

    events(u).count(hasClashed(_, events(u).toList))!=0
  }

  def modifyEvent(u: User, eventTitle: String, newEvent: Event): Unit = {
    events(u)=events(u).map(i => if (i.title==eventTitle) newEvent else i)
  }

  def findEventByTitle(u: User, title: String): Event = events(u).find(_.title==title).get
}
