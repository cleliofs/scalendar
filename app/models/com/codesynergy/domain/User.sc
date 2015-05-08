import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(
                 var username: String = "",
                 var name: String = "",
                 var surname: String = "",
                 var email: String = "",
                 var company: String = "") extends Ordered[User] {

  override def toString: String = s"Username: $username - Email: $email ($name $surname - $company)"

  override def compare(that: User): Int = this.username.compareTo(that.username)

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: User => username == that.username
    case _ => false
  }


}


object User {

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


val u1 = User("cleliofs")
val u2 = User("josi")
val u3 = User("marcel")
val u4 = User("newuser")
val u5 = User("auser")
u1 == u2
val list = List(u1,u2,u3,u4,u5).sorted
println(Json.toJson(list))
Json.toJson(List(u1))

