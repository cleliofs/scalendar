import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(
                 var username: String = "",
                 var name: String = "",
                 var surname: String = "",
                 var email: String = "",
                 var company: String = "") {

  override def toString: String = s"Username: $username - Email: $email ($name $surname - $company)"

//  def json: JsValue = Json.toJson(this)
}


object User {
//  implicit val userFormat = Json.format[User]

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "username" -> user.username,
      "name" -> user.name,
      "surname" -> user.surname,
      "email" -> user.email,
      "company" -> user.company
    )
  }
  implicit  val userReads: Reads[User] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "surname").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "company").read[String]
    )(User.apply(_, _, _ ,_ ,_))
}


val u1 = User("cleliofs", "Clelio", "De Souza")
val u2 = User("newuser", "Test", "De Souza")
val list = List(u1,u2)
println(Json.toJson(list))
Json.toJson(List(u1))
