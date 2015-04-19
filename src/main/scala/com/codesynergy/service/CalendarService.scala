package main.scala.com.codesynergy.service

import main.scala.com.codesynergy.domain.{Event, User}

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {

  private var users: List[User] = Nil

  var events: Map[User, List[Event]] = Map()

  def find(u: User): Option[User] = {
    users.find(e => e.username == u.username)
  }

  def save(u: User): Unit = {
    users = u :: users
  }

  def showUsers: Unit = users.foreach(println)

  def addEvent(u: User, e: Event): Unit = {
    if (events(u).isEmpty) e :: events(u)
    events += (u -> events)
  }
}
