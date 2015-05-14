package controllers

import main.scala.com.codesynergy.service.CalendarService
import models.com.codesynergy.domain.User
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import views.html

object Application extends Controller {

  val calendarService = new CalendarService

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

  def getUserByUsername(username: String) = Action {
    calendarService.getUserByUsername(username).map { user: User =>
      Ok(Json.toJson(user))
    }.getOrElse {
      NotFound(Json.toJson(
        Map("status" -> "OK", "message" -> s"User not found [$username]")
      )
      )
    }
  }

  def saveUser = Action(parse.json) { request =>
    val result = request.body.validate[User]
    result.fold(
      errors => BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors))),
      user => {
        if (calendarService.exists(user)) {
          BadRequest(Json.obj("Status" -> "302 (Found)", "message" -> (user + " user already exists.")))
        } else {
          calendarService.save(user)
          Ok(Json.obj("status" -> "OK", "message" -> (user + " saved.")))
        }

      }
    )

  }

  def updateUser(username: String) = Action(parse.json) { request =>
    val result = request.body.validate[User]
    result.fold(
      errors => BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors))),
      user => {
        if (!calendarService.exists(user)) {
          BadRequest(Json.obj("Status" -> "404 (Not Found)", "message" -> (user + " user not found.")))
        } else {
          calendarService.updateUser(username, user)
          Ok(Json.obj("status" -> "OK", "message" -> (user + " saved.")))
        }
      }
    )

  }

  def updateUserEmail(username: String) = Action(parse.json) { request =>
    val result = request.body.validate[User]
    result.fold(
      errors => BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors))),
      user => {
        if (!calendarService.exists(user)) {
          BadRequest(Json.obj("Status" -> "404 (Not Found)", "message" -> (user + " user not found.")))
        } else {
          calendarService.updateUserEmail(username, user.email)
          Ok(Json.obj("status" -> "OK", "message" -> (user + " saved.")))
        }
      }
    )

  }



}