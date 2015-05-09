import main.scala.com.codesynergy.domain.User
import models.com.codesynergy.domain.{Calendar, DB}
import play.api.GlobalSettings

/**
 * Created by clelio on 09/05/15.
 */
object Global extends GlobalSettings {

  val u1 = User("cleliofs", "Clelio", "De Souza", "cleliofs@gmail.com", "Code Synergy")
  val u2 = User("josi", "Josirene", "Souza")
  val u3 = User("marcel", "Marcel", "Sato")

  // create users
  var newUsers: Set[User] = Set()
  newUsers += DB.save(u1)
  newUsers += DB.save(u2)
  newUsers += DB.save(u3)

  // create calendar with users
  DB.save(Calendar("New Calendar", newUsers))

//  DB.query[Calendar].fetch().map(c => c.copy(users = c.users + persistedU1)).map(DB.save)
}
