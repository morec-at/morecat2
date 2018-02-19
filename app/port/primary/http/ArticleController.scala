package port.primary.http

import javax.inject.{Inject, Singleton}

import application.blog.ArticleResolveService
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import port.primary.http.response.ArticleResponse

import scala.concurrent.ExecutionContext

@Singleton
class ArticleController @Inject()(cc: ControllerComponents, service: ArticleResolveService)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  def getArticleList() = Action.async {
    service.resolveAll map { articles =>
      Ok(Json.toJson(articles.map(ArticleResponse(_))))
    }
  }

}
