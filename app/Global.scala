import models.com.codesynergy.domain._
import play.api.GlobalSettings
import slick.dbio.DBIO
import slick.driver.H2Driver.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by clelio on 09/05/15.
 */
object Global extends GlobalSettings {

  val c = Calendar("New Calendar")

  val u1 = User("cleliofs", "Clelio", "De Souza", "cleliofs@gmail.com", "Code Synergy")
  val u2 = User("josi", "Josirene", "Souza")
  val u3 = User("marcel", "Marcel", "Sato")

  val db = Database.forConfig("h2file")
  try {

    val setupAction: DBIO[Unit] = DBIO.seq(
      // create schema
      (CalendarTable.calendar.schema ++ UserTable.users.schema ++ EventTable.events.schema).create,

      // insert calendar
      CalendarTable.calendar += c,

      // insert users
      UserTable.users ++= Seq(u1, u2, u3)
    )

    val f: Future[Unit] = db.run(setupAction)
    Await.result(f, Duration.Inf)

  } finally db.close


}
