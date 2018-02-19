package port.primary.http

import java.time.LocalDateTime

import application.blog.ArticleResolveService
import domain.model.blog._
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ArticleControllerSpec extends FlatSpec with MockitoSugar with DiagrammedAssertions {

  "getArticleList" should "get article list" in new Context {
    val result = sut.getArticleList()(FakeRequest())
    assert(status(result) === OK)

    val responseBody = contentAsJson(result)
    assert((responseBody \\ "id").size === fixtureSize)
  }

  trait Context {
    val mockService = mock[ArticleResolveService](RETURNS_SMART_NULLS)
    val sut         = new ArticleController(stubControllerComponents(), mockService)

    val fixtureSize = 5
    val articles = for (_ <- 1 to fixtureSize) yield {
      Article(
        id = ArticleId("dummy"),
        title = ArticleTitle("dummy"),
        content = ArticleContent("dummy"),
        createdDateTime = ArticleCreatedDateTime(LocalDateTime.now),
        lastUpdatedDateTime = ArticleLastUpdatedDateTime(LocalDateTime.now)
      )
    }

    when(mockService.resolveAll()) thenReturn Future(articles)
  }

}
