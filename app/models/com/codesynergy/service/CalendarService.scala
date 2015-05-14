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
    val q = for (c <- Calendar.calendar) yield c
    val f: Future[Seq[Calendar]] = db.run(q.result)
    Await.result(f, Duration.Inf)
  }

  def getUsers: Seq[User] = {
    Await.result(db.run(User.users.result), Duration.Inf)
  }

  def getUserByUsername(username: String) = {
//    val user = for {
//      username <- Parameters[String]
//      u <- User.users if u.username == username
//    } yield u

    def userByUsername(username: Rep[String]) = for {
      u <- User.users if u.username == username
    } yield u

    val userByUsernameCompiled = Compiled(userByUsername _)

    userByUsernameCompiled(username).run

//    Await.result(db.run(user(username).result), Duration.Inf)
  }

}
