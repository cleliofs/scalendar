package models.com.codesynergy.domain

import main.scala.com.codesynergy.domain.{Event, User}
import sorm._

/**
 * Created by clelio on 09/05/15.
 */
object Db extends Instance(
  entities = Set(
    Entity[User](unique = Set() + Seq("username")),
    Entity[Event]()
  ),
  url = "jdbc:h2:mem:scalendar",
  user = "",
  password = "",
  initMode = InitMode.Create
)
