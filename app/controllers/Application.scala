package controllers

import main.scala.com.codesynergy.service.CalendarService
import play.api.libs.json.Json
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


}