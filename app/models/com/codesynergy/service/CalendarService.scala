package main.scala.com.codesynergy.service

import models.com.codesynergy.domain.{Calendar, CalendarTable}
import slick.driver.H2Driver.api._

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration

/**
 * Created by clelio on 19/04/15.
 */
class CalendarService {
  val db = Database.forConfig("h2file")

  def getCalendar = {
//    try {
//      val f: Future[_] = {
//        db.run(DBIO.seq(
//          Calendar.calendar
//        ))
//      }
//      Await.result(f, Duration.Inf)
//
//    } finally db.close

    try {
      val q = for (c <- Calendar.calendar) yield c
      val a = q.result
      val f: Future[Seq[Calendar]] = db.run(a)
      Await.result(f, Duration.Inf)
    } finally db.close

  }

//  def getCalendar = Calendar("New Calendar")


}
