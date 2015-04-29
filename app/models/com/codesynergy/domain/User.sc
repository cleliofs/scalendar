import play.api.libs.json.{JsValue, Json, Writes}

/**
 * Created by clelio on 19/04/15.
 */
case class User(
                 var username: String = "",
                 var name: String = "",
                 var surname: String = "",
                 var email: String = "",
                 var company: String = "") {

  override def toString: String = s"Username: $username - Email: $email ($name $surname - $company)"

  def json: JsValue = Json.toJson(this)

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "username" -> user.username,
      "name" -> user.name,
      "surname" -> user.surname,
      "email" -> user.email,
      "company" -> user.company
    )
  }
}

val u1 = User("cleliofs", "Clelio", "De Souza")
val u2 = User("newuser", "Test", "De Souza")
val list = List(u1,u2)
list.map(_.json).mkString("{", ",", "}")
