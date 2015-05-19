package main.scala.com.codesynergy.service

import models.com.codesynergy.domain.{Calendar, User}
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {
  val db = Database.forConfig("h2mem1")

  def getCalendar: Future[Seq[Calendar]] = {
    val q = Calendar.calendar
    val f: Future[Seq[Calendar]] = db.run(q.result)
    f
  }

  def getUsers: Future[Seq[User]] = {
    val q = User.users

    val eventualUnit: Future[Seq[User]] = Future {
      println("Do something expensive first...")
      Thread.sleep(1500)
      Seq()
    }
    for {
      e <- eventualUnit
      r <- db.run(q.result)
    } yield r

//    db.run(q.result)
  }

  def getUserByUsername(username: String): Future[Seq[User]] = {
    val userByUsernameCompiled = Compiled { username: Rep[String] =>
      User.users.filter(_.username === username)
    }

    val q = userByUsernameCompiled(username)

    val f: Future[Seq[User]] = db.run(q.result)
    f
  }

  def existsWithoutAwait(user: User) = {
    val q = User.users.filter(_.username === user.username).exists
    db.run(q.result)
  }

  def exists(user: User) = {
    val q = User.users.filter(_.username === user.username).exists
    import scala.concurrent.duration._
    import scala.concurrent.Await
    Await.result(db.run(q.result), Duration.Inf)
  }

  def save(user: User) = {
    db.run(User.users += user)
  }

  def updateUser(username: String, user: User) = {
    db.run(User.users.filter(_.username === username).map(u => u).update(user))
  }

  def updateUserEmail(username: String, email: String) = {
    db.run(User.users.filter(_.username === username).map(u => u.email).update(email))
  }

}

