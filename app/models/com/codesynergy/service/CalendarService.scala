package main.scala.com.codesynergy.service

import models.com.codesynergy.domain.{Calendar, User}
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

  def getUserByUsername(username: String) = {

    val userByUsernameCompiled = Compiled { username: Rep[String] =>
      User.users.filter(_.username === username)
    }

    val q = userByUsernameCompiled(username)

    Await.result(db.run(q.result), Duration.Inf)

  }

}
