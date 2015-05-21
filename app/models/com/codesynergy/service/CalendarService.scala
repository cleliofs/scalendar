package main.scala.com.codesynergy.service

import models.com.codesynergy.domain.{Event, Calendar, User}
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {
  val db = Database.forConfig("h2mem1")

  def getCalendar: Future[Seq[Calendar]] = {
    val q = Calendar.query
    val f: Future[Seq[Calendar]] = db.run(q.result)
    f
  }

  def getCalendarUsers(calendarId: Int): Future[Seq[User]] = {
    val q = User.query.filter(u => u.calendarId === calendarId)
    db.run(q.result)
  }

  def getUsers: Future[Seq[User]] = {
    val q = User.query

    val eventualUnit: Future[Seq[User]] = Future {
      println("Do something expensive first...")
      Thread.sleep(1500)
      Seq()
    }
    for {
      e <- eventualUnit
      r <- db.run(q.result)
    } yield r
  }

  def getUserByUsername(username: String): Future[Seq[User]] = {
    val userByUsernameCompiled = Compiled { username: Rep[String] =>
      User.query.filter(_.username === username)
    }

    val q = userByUsernameCompiled(username)

    val f: Future[Seq[User]] = db.run(q.result)
    f
  }

  def existsWithoutAwait(user: User) = {
    val q = User.query.filter(_.username === user.username).exists
    db.run(q.result)
  }

  def exists(user: User) = {
    val q = User.query.filter(_.username === user.username).exists
    import scala.concurrent.duration._
    import scala.concurrent.Await
    Await.result(db.run(q.result), Duration.Inf)
  }

  def userExistsInCalendar(user: User) = {
    ???
  }

  def save(user: User) = {
    db.run(User.query += user)
  }

  def updateUser(username: String, user: User) = {
    db.run(User.query.filter(_.username === username).map(u => u).update(user))
  }

  def updateUserEmail(username: String, email: String) = {
    db.run(User.query.filter(_.username === username).map(u => u.email).update(email))
  }

  def addUserToCalendar(username: String, calendarId: Int) = {
    db.run(User.query.filter(_.username === username).map(u => u.calendarId).update(Some(calendarId)))
  }

  def getEventsByUsername(username: String) = {
    val q1 = User.findEventsUserIsOwner(username)
    val q2 = User.findEventsForUser(username)
    db.run(((q1 union q2).sortBy(e => e.id)).result)
  }

}

