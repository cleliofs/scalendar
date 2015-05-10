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

//  def getCalendar: Calendar = {
//    try {
//      val f: Future[_] = {
//        db.run(DBIO.seq(
//          CalendarTable.calendar.result
//        ))
//      }
//      Await.result(f, Duration.Inf)
//
//    } finally db.close
//
//  }

  def getCalendar = Calendar


}
