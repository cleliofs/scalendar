package controllers

import main.scala.com.codesynergy.service.CalendarService
import models.com.codesynergy.domain.{Calendar, User}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, AnyContent, Controller, Result}
import views.html
import scala.concurrent.duration._

import scala.concurrent.Future

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

  def getCalendar: Action[AnyContent] = Action.async {
    val calendarFuture: Future[Seq[Calendar]] = Future { calendarService.getCalendar }
    val resultFuture: Future[Result] = calendarFuture.map(c => Ok(Json.toJson(c)))
    resultFuture
  }

  def getUsers: Action[AnyContent] = Action.async {
    val usersFuture: Future[Seq[User]] = scala.concurrent.Future { calendarService.getUsers }
    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Timeout", 2 second)
    Future.firstCompletedOf(Seq(usersFuture, timeoutFuture)).map {
      case users: Seq[User] => Ok(Json.toJson(users))
      case t: String => InternalServerError(t)
    }
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