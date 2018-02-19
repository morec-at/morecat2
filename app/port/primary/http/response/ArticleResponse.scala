package port.primary.http.response

import java.time.LocalDateTime

import domain.model.blog._
import play.api.libs.json.Json

case class ArticleResponse(id: String,
                           title: String,
                           content: String,
                           createdDateTime: LocalDateTime,
                           lastUpdatedDateTime: LocalDateTime)

object ArticleResponse {

  def apply(article: Article): ArticleResponse =
    ArticleResponse(
      id = article.id.value,
      title = article.title.value,
      content = article.content.value,
      createdDateTime = article.createdDateTime.value,
      lastUpdatedDateTime = article.lastUpdatedDateTime.value
    )

  implicit val jsonWrites = Json.writes[ArticleResponse]

}
