package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.data.Forms._
import play.api.data.Form
import models.MemberService

@Singleton
class HomeController @Inject() (ms: MemberService) extends Controller {

  val joinForm = Form(
    mapping(
      "mid" -> ignored(0),
      "userid" -> nonEmptyText,
      "password" -> nonEmptyText,
      "nickname" -> nonEmptyText,
      "email" -> nonEmptyText,
      "regdate" -> optional(date("yyyy-MM-dd")))(models.Member.apply)(models.Member.unapply _))

  def index = Action {
    Ok(views.html.index("index"))
  }

  def form = Action {
    Ok(views.html.form(joinForm))
  }

//  def uploadFile = Action(parse.multipartFormData) { request =>
//    request.body.file("picture").map { picture =>
//      import java.io.File
//      val filename = picture.filename
//      picture.ref.moveTo(new File(s"/tmp/picture/$filename"))
//      Ok("파일 업로드 완료")
//    }.getOrElse {
//      Redirect(routes.HomeController.index).flashing(
//        "error" -> "파일이 없습니다")
//    }
//  }

  def list = Action {
    Ok(views.html.list(ms.getList))
  }

  def save = Action { implicit request =>
    val form = joinForm.bindFromRequest()
    form.fold(
      hasErrors => {
        println(hasErrors)
        Redirect(routes.HomeController.form)
      },
      member => {
        ms.insert(member)
        Redirect(routes.HomeController.list)
      })
  }

}
