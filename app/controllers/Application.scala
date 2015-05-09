package controllers

import main.scala.com.codesynergy.domain.{Event, User}
import main.scala.com.codesynergy.service.CalendarService
import play.api.libs.json.{JsError, JsResult, Json}
import play.api.mvc.{Action, Controller}
import views.html

object Application extends Controller {

  val calendarService = new CalendarService
//
//  val u1 = User("cleliofs", "Clelio", "De Souza", "cleliofs@gmail.com", "Code Synergy")
//  val u2 = User("josi", "Josirene", "Souza")
//  val u3 = User("marcel", "Marcel", "Sato")
//
//  calendarService.save(u1)
//  calendarService.save(u2)
//  calendarService.save(u3)

  def index = Action {
    Ok(views.html.index("Hi Clelio, your new application is ready."))
  }


  def hello = Action {
    Ok(html.hello("Hi Clelio!")("Hello world, Clelio!"))
  }

  def sayHello = Action(parse.json) { request =>
      (request.body \ "name").asOpt[String].map { name: String =>
        Ok(Json.toJson(Map("status" -> "OK", "message" -> (s"Hello $name"))))
      }.getOrElse {
        BadRequest(Json.toJson(
          Map("status" -> "400", "message" -> "Missing parameter [name]")
          )
        )
      }
  }

  def getCalendar = Action { request =>
    Ok(Json.toJson(calendarService.getCalendar))

  }

  def getUsers = Action { request =>
    Ok(Json.toJson(calendarService.getUsers))
  }

  def getUserByUsername(username: String) = Action { request =>
    calendarService.findUserByUsername(User(username)).map { user: User =>
      Ok(Json.toJson(user))
    }.getOrElse {
      NotFound(Json.toJson(
          Map("status" -> "OK", "message" -> s"User not found [$username]")
        )
      )
    }

  }

  def getEventsByUsername(username: String) = Action { request =>
    calendarService.findUserByUsername(User(username)).map { user =>
      Ok(Json.toJson(user.events))
    }.getOrElse {
      NotFound(Json.toJson(
        Map("status" -> "OK", "message" -> s"User not found [$username]")
      ))
    }
  }


  def saveUser = Action(parse.json) { request =>
   val result: JsResult[User] = request.body.validate[User]
    result.fold(
      errors => BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors))),
      user => {
        calendarService.save(user)
        Ok(Json.obj("status" -> "OK", "message" -> (user + " saved.")))
      }
    )
  }

  def addEvent(username: String) = Action(parse.json) { request =>
    val result = request.body.validate[Event]
    result.fold(
      errors => {
        BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors)))
      },
      event => {
        calendarService.addEventToUser(User(username), event)
        Ok(Json.obj("status" -> "OK", "message" -> (event + s" saved to user $username")))
      }
    )
  }

}