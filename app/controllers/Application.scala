package controllers

import main.scala.com.codesynergy.domain.User
import main.scala.com.codesynergy.service.CalendarService
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
        Ok(s"Hello $name\n")
      }.getOrElse {
        BadRequest("Missing parameter [name]")
      }
  }

  def users = Action {
//    (request.body \ "name").asOpt[String].map { name =>
//      Ok(toJson(
//        Map("status" -> "OK", "message" -> ("Hello " + name))
//      ))
//    }.getOrElse {
//      BadRequest(toJson(
//        Map("status" -> "OK", "message" -> "Missing parameter [name]")
//      ))
//    }
    Ok(html.users(c.getUsers.map(_.json).mkString("{", ",", "}")))
  }

}