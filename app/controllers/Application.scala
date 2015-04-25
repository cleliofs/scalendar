package controllers

import play.api.mvc.{Action, Controller}
import scala.

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
//  OK(200)
  }

}