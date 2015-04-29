package controllers

import main.scala.com.codesynergy.domain.User
import main.scala.com.codesynergy.service.CalendarService
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import views.html

object Application extends Controller {

  val u1 = User("cleliofs", "Clelio", "De Souza", "cleliofs@gmail.com", "Code Synergy")
  val u2 = User("newuser", "New", "User")

  val c = new CalendarService
  c.save(u1)
  c.save(u2)

  def index = Action {
    Ok(views.html.index("Hi Clelio, your new application is ready."))
  }


  def hello = Action {
    Ok(html.hello("Hi Clelio!")("Hello world, Clelio!"))
  }

  def sayHello = Action(parse.json) { request =>
      (request.body \ "name").asOpt[String].map { name =>
        Ok(Json.toJson(Map("status" -> "OK", "message" -> (s"Hello $name"))))
      }.getOrElse {
        BadRequest(Json.toJson(
          Map("status" -> "OK", "message" -> "Missing parameter [name]")))
      }
  }

  def getUsers = Action { request =>
    Ok(Json.toJson(c.getUsers.map(_.json).mkString("{", ",", "}")))
  }

  def saveUser = Action(parse.json) { request =>
   val result = request.body.validate[User]
    result.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      user => {
        c.save(user)
        Ok(Json.obj("status" -> "OK", "message" -> (user + " saved.")))
      }
    )
  }

}