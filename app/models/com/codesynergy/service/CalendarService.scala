package main.scala.com.codesynergy.service

import models.com.codesynergy.domain.{UserTable, Calendar, User}
import slick.driver.H2Driver.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {
  val db = Database.forConfig("h2mem1")

  def getCalendar: Seq[Calendar] = {
    val q = Calendar.calendar
    val f: Future[Seq[Calendar]] = db.run(q.result)
    Await.result(f, Duration.Inf)
  }

  def getUsers: Seq[User] = {
    val q = User.users
    Await.result(db.run(q.result), Duration.Inf)
  }

  def getUserByUsername(username: String): Option[User] = {

    val userByUsernameCompiled = Compiled { username: Rep[String] =>
      User.users.filter(_.username === username)
    }

    val q = userByUsernameCompiled(username)

    Await.result(db.run(q.result), Duration.Inf).headOption
  }

  def exists(user: User) = {
    val q = User.users.filter(_.username === user.username).exists
    Await.result(db.run(q.result), Duration.Inf)
  }

  def save(user: User) = {
    Await.result(db.run(User.users += user), Duration.Inf)
  }

  def updateUser(username: String, user: User) = {
    Await.result(db.run(User.users.filter(_.username === username).map(u => u).update(user)), Duration.Inf)
  }

  def updateUserEmail(username: String, email: String) = {
    Await.result(db.run(User.users.filter(_.username === username).map(u => u.email).update(email)), Duration.Inf)
  }

}
