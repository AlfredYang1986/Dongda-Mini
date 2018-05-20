package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, Controller}

@Singleton
class PageController @Inject() extends Controller {

    def business = Action {
		Ok(views.html.business())
	}

    def activity(provider_id : String) = Action {
        Ok(views.html.activity(provider_id))
    }

    def questions = Action {
        Ok(views.html.question())
    }
}