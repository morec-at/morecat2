package morecat.usecase.article

import java.time.Instant

import cats.implicits._
import cats.{Applicative, Id}
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleStatus.Public
import morecat.domain.model.article._
import morecat.usecase.article.ResolveArticle.{ArticleNotFound, ArticleResolved}
import org.scalatest.{DiagrammedAssertions, FlatSpec}

class ResolveArticleSpec extends FlatSpec with DiagrammedAssertions {

  "Normal case" should "resolve an article by its Id" in {
    val result = normalUseCase
      .exec(testArticle.id)
      .run(Applicative[Id].pure)

    result.map {
      case event: ArticleResolved => assert(event.data.id === testArticle.id)
      case err                    => fail(err.toString)
    }
  }

  "Abnormal case: Not found the article of the id" should "fail to resolve" in {
    val result = abnormalUseCase
      .exec(ArticleId())
      .run(Applicative[Id].pure)

    assert(result === ArticleNotFound)
  }

  private val canResolveArticleRepository: ArticleRepository[Id] = new ArticleRepository[Id] {
    override def resolveById(id: ArticleId): Id[Option[Article]] = Some(testArticle)
    override def store(article: Article): Id[Unit]               = ()
  }

  private val normalUseCase = new ResolveArticle[Id](canResolveArticleRepository)

  private val canNotResolveArticleRepository: ArticleRepository[Id] = new ArticleRepository[Id] {
    override def resolveById(id: ArticleId): Id[Option[Article]] = None
    override def store(article: Article): Id[Unit]               = ()
  }

  private val abnormalUseCase = new ResolveArticle[Id](canNotResolveArticleRepository)

  private val testArticle = Article
    .create(
      status = Public,
      titleResult = ArticleTitle.of("A title"),
      contentResult = ArticleContent.of("A content."),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(Instant.now),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
    )
    .fold(
      e => fail(e.toString),
      a => a
    )

}
