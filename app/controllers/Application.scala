package controllers

import main.scala.com.codesynergy.service.CalendarService
import models.com.codesynergy.domain.{Event, Calendar, User}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsError, JsResult, Json}
import play.api.mvc.{Action, AnyContent, Controller, Result}
import views.html

import scala.concurrent.Future
import scala.concurrent.duration._

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
        Ok(Json.obj("status" -> OK, "message" -> (s"Hello $name")))
      }.getOrElse {
        BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "Missing parameter [name]"))
      }
  }

  def getCalendar: Action[AnyContent] = Action.async {
    val calendarFuture: Future[Seq[Calendar]] = calendarService.getCalendar
    val resultFuture: Future[Result] = calendarFuture.map(c => Ok(Json.toJson(c)))
    resultFuture
  }

  def getCalendarUsers(id: Int) = Action.async {
    calendarService.getCalendarUsers(id).map(u => Ok(Json.toJson(u)))
  }

  def getUsers: Action[AnyContent] = Action.async {
    val usersFuture: Future[Seq[User]] = calendarService.getUsers recover {
      case e: Throwable => Seq()
    }

    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Timeout", 2 second)
    Future.firstCompletedOf(Seq(usersFuture, timeoutFuture)).map {
      case users: Seq[User] if users.nonEmpty => Ok(Json.toJson(users))
      case Seq() => InternalServerError(Json.obj("status" -> INTERNAL_SERVER_ERROR, "message" -> "Internal Error: Empty results"))
      case t: String => InternalServerError(t)
    }
  }

  def getUserByUsername(username: String) = Action.async {
    val userFuture: Future[Option[User]] = calendarService.getUserByUsername(username) map { s: Seq[User] => s.headOption }
    val resultFuture: Future[Result] = userFuture map {
      case u: Option[User] if u.nonEmpty => Ok(Json.toJson(u.get))
      case None => NotFound(Json.obj("status" -> OK, "message" -> s"User not found [$username]"))
    }

    resultFuture
  }

  def saveUserWithoutAwait = Action.async(parse.json) { request =>
    val validateFuture: Future[JsResult[User]] = Future { request.body.validate[User] }
    def existsFuture(user: User): Future[Boolean] = calendarService.existsWithoutAwait(user)
    def saveUserFuture(user: User) = calendarService.save(user)

    val eventualResult: Future[Result] = for {
      v <- validateFuture if v.isSuccess
      e <- existsFuture(v.get) if e == false
    } yield {
        saveUserFuture(v.get)
        Ok(Json.obj("status" -> OK, "message" -> (v.get + " saved.")))
      }
    eventualResult recover {
      case _ => BadRequest(Json.obj("Status" -> s"$FOUND (Found)", "message" -> (" user already exists.")))
    }


  }

  def saveUser = Action(parse.json) { request =>
    val result = request.body.validate[User]
    result.fold(
      errors => BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> JsError.toFlatJson(errors))),
      user => {
        if (calendarService.exists(user)) {
          BadRequest(Json.obj("Status" -> s"$FOUND (Found)", "message" -> (user + " user already exists.")))
        } else {
          calendarService.save(user)
          Ok(Json.obj("status" -> OK, "message" -> (user + " saved.")))
        }

      }
    )

  }


  def updateUser(username: String) = Action.async(parse.json) { request =>
    Future {
      val result = request.body.validate[User]
      result.fold(
        errors => BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors))),
        user => {
          if (!calendarService.exists(user)) {
            BadRequest(Json.obj("Status" -> "404 (Not Found)", "message" -> (user + " user not found.")))
          } else {
            calendarService.updateUser(username, user)
            Ok(Json.obj("status" -> OK, "message" -> (user + " saved.")))
          }
        }
      )
    }
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
          Ok(Json.obj("status" -> OK, "message" -> (user + " saved.")))
        }
      }
    )

  }

  def getEventsByUsername(username: String) = Action.async {
    val eventsFuture: Future[Seq[Event]] = calendarService.getEventsByUsername(username)
    val resultFuture: Future[Result] = eventsFuture map {
      e => Ok(Json.toJson(e))
    }
    resultFuture
  }

  def addUserToCalendar(calendarId: Int) = Action(parse.json) { request =>
    val result = request.body.validate[User]
    result.fold(
      errors => BadRequest(Json.obj("status" -> "400", "message" -> JsError.toFlatJson(errors))),
      user => {
        if (!calendarService.exists(user)) {
          BadRequest(Json.obj("Status" -> "404 (Not Found)", "message" -> (user + " user not found.")))
        } else {
          calendarService.addUserToCalendar(user.username, calendarId)
          Ok(Json.obj("status" -> OK, "message" -> (user + " added to calendar.")))
        }
      }
    )
  }


}