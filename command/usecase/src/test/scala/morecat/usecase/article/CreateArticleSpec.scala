package morecat.usecase.article

import java.time.Clock

import cats.implicits._
import cats.{Applicative, Id}
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleContent.ArticleContentBlank
import morecat.domain.model.article.ArticleStatus.Public
import morecat.domain.model.article.ArticleTags.ArticleTagBlank
import morecat.domain.model.article.ArticleTitle.ArticleTitleBlank
import morecat.domain.model.article.{Article, ArticleId, ArticleRepository}
import morecat.usecase.InvalidParameters
import morecat.usecase.article.CreateArticle.ArticleCreated
import org.scalatest.{DiagrammedAssertions, FlatSpec}

class CreateArticleSpec extends FlatSpec with DiagrammedAssertions {

  "Normal case" should "create an article" in {
    val result = useCase
      .exec(
        status = Public,
        title = "A title",
        content = "A content.",
        comment = "first",
        editorId = AccountId("editor-id-01"),
        tags = Set.empty
      )
      .run(Applicative[Id].pure)

    result.map {
      case event: ArticleCreated =>
        assert(event.data.revisions.size eqv 1)
      case _ => fail()
    }
  }

  "Abnormal case: Blank title" should "fail to create" in {
    val result = useCase
      .exec(
        status = Public,
        title = "",
        content = "A content.",
        comment = "first",
        editorId = AccountId("editor-id-01"),
        tags = Set.empty
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleTitleBlank))
      case _ => fail()
    }
  }

  "Abnormal case: Blank content" should "fail to create" in {
    val result = useCase
      .exec(
        status = Public,
        title = "A title",
        content = "",
        comment = "first",
        editorId = AccountId("editor-id-01"),
        tags = Set.empty
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleContentBlank))
      case _ => fail()
    }
  }

  "Abnormal case: Blank tag string" should "fail to create" in {
    val result = useCase
      .exec(
        status = Public,
        title = "A title",
        content = "A content",
        comment = "first",
        editorId = AccountId("editor-id-01"),
        tags = Set("")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleTagBlank))
      case _ => fail()
    }
  }

  "Abnormal case: Blank title, content, tag string" should "fail to create" in {
    val result = useCase
      .exec(
        status = Public,
        title = "",
        content = "",
        comment = "first",
        editorId = AccountId("editor-id-01"),
        tags = Set("")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleTitleBlank, ArticleContentBlank, ArticleTagBlank))
      case _ => fail()
    }
  }

  private val mockRepository: ArticleRepository[Id] = new ArticleRepository[Id] {
    override def resolveById(id: ArticleId): Id[Option[Article]] = None
    override def store(article: Article): Id[Unit]               = ()
  }

  private val useCase = new CreateArticle[Id](mockRepository, Clock.systemUTC())

}
