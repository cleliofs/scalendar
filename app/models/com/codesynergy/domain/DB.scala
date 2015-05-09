package models.com.codesynergy.domain

import main.scala.com.codesynergy.domain.{Event, User}
import sorm._

/**
 * Created by clelio on 09/05/15.
 */
object DB extends Instance(
  entities = Set(
    Entity[Calendar](unique = Set() + Seq("name")),
    Entity[User](unique = Set() + Seq("username")),
    Entity[Event]()
  ),
//  url = "jdbc:h2:file:./h2/scalendar",
  url = "jdbc:h2:mem:scalendar",
  user = "sa",
  password = "",
  initMode = InitMode.Create
)
