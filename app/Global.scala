import java.sql.Date

import models.com.codesynergy.domain._
import play.api.GlobalSettings
import slick.dbio.DBIO
import slick.driver.H2Driver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by clelio on 09/05/15.
 */
object Global extends GlobalSettings {

  val c = Calendar(1, "New Calendar")

  val u1 = User(1, "cleliofs", "Clelio", "De Souza", "cleliofs@gmail.com", "Code Synergy", Some(1))
  val u2 = User(2, "josi", "Josirene", "Souza")
  val u3 = User(3, "marcel", "Marcel", "Sato")

  val e1 = Event(1, "First Event", 1)
  val e2 = Event(2, "Second Event", 1)
  val e3 = Event(3, "Third Event", 2)

  val u1_e1 = UserEvent(1, 1)
  val u1_e2 = UserEvent(1, 2)
  val u2_e1 = UserEvent(2, 1)


  val db = Database.forConfig("h2mem1")
  try {

      val setupAction: DBIO[Unit] = DBIO.seq(
        // drop schema if exists
        (Calendar.query.schema ++ User.query.schema ++ Event.query.schema ++ UserEvent.query.schema).drop,

        // create schema
        (Calendar.query.schema ++ User.query.schema ++ Event.query.schema ++ UserEvent.query.schema).create,

        // insert calendar
        Calendar.query += c,

        // insert users
        User.query ++= Seq(u1, u2, u3),

        // insert events
        Event.query ++= Seq(e1, e2, e3),

        // insert users_events
        UserEvent.query ++= Seq(u1_e1, u1_e2, u2_e1)
      )

      val f: Future[Unit] = db.run(setupAction)
      Await.result(f, Duration.Inf)

  } finally db.close


}
