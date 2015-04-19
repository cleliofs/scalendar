package test.scala.com.codesynergy.domain

import main.scala.com.codesynergy.domain.User
import org.scalatest.FlatSpec

/**
 * Created by clelio on 19/04/15.
 */
class UserTest extends FlatSpec {

  "A User" should "contain 'username', 'name', 'surname', 'email', and company" in {
    val u = new User
    u.username="cleliofs"
    u.name="clelio"
    u.surname="de souza"
    u.email="cleliofs@gmail.com"
    u.company="code synergy"
    assert("cleliofs"==u.username)
    assert("clelio"==u.name)
    assert("de souza"==u.surname)
    assert("cleliofs@gmail.com"==u.email)
    assert("code synergy"==u.company)
  }

  "A User" should "have an apply method" in {
    val u = User("cleliofs")
    assert("cleliofs"==u.username)
  }

  "A User" should "have an equility method as '=='" in {
    val u = User("cleliofs")
    assert(User("cleliofs")==u)
  }


}
