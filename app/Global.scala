import main.scala.com.codesynergy.domain.User
import models.com.codesynergy.domain.Db

/**
 * Created by clelio on 09/05/15.
 */
object Global {

  val u1 = User("cleliofs", "Clelio", "De Souza", "cleliofs@gmail.com", "Code Synergy")
  val u2 = User("josi", "Josirene", "Souza")
  val u3 = User("marcel", "Marcel", "Sato")

  // create users
  Db.save(Seq(u1, u2, u3))

}
