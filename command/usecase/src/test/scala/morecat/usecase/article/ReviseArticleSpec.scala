package morecat.usecase.article

import java.time.{Clock, Instant, ZoneId, ZonedDateTime}

import cats.implicits._
import cats.{Applicative, Id}
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleContent.ArticleContentBlank
import morecat.domain.model.article.ArticleRevisions.CreatedAtBeforeLatest
import morecat.domain.model.article.ArticleStatus.{Private, Public}
import morecat.domain.model.article.ArticleTags.ArticleTagBlank
import morecat.domain.model.article.ArticleTitle.ArticleTitleBlank
import morecat.domain.model.article._
import morecat.usecase.article.ReviseArticle.ArticleRevised
import morecat.usecase.{Fatal, InvalidParameters}
import org.scalatest.{DiagrammedAssertions, FlatSpec}

class ReviseArticleSpec extends FlatSpec with DiagrammedAssertions {

  "Normal case" should "revise an article" in {
    val result = useCase
      .exec(
        article = initialArticle,
        status = Private,
        title = "An updated title",
        content = "An updated content.",
        comment = "second",
        editorId = AccountId("editor-id-02"),
        tags = Set("tag1", "tag2")
      )
      .run(Applicative[Id].pure)

    result.map {
      case event: ArticleRevised =>
        assert(event.data.revisions.size eqv 2)
      case _ => fail(result.toString)
    }
  }

  "Abnormal case: Blank title" should "fail to revise" in {
    val result = useCase
      .exec(
        article = initialArticle,
        status = Private,
        title = "",
        content = "An updated content.",
        comment = "second",
        editorId = AccountId("editor-id-02"),
        tags = Set("tag1", "tag2")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleTitleBlank))
      case _ => fail(result.toString)
    }
  }

  "Abnormal case: Blank content" should "fail to revise" in {
    val result = useCase
      .exec(
        article = initialArticle,
        status = Private,
        title = "An updated title",
        content = "",
        comment = "second",
        editorId = AccountId("editor-id-02"),
        tags = Set("tag1", "tag2")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleContentBlank))
      case _ => fail(result.toString)
    }
  }

  "Abnormal case: Blank tag string" should "fail to revise" in {
    val result = useCase
      .exec(
        article = initialArticle,
        status = Private,
        title = "An updated title",
        content = "An updated content.",
        comment = "second",
        editorId = AccountId("editor-id-02"),
        tags = Set("", "tag2")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleTagBlank))
      case _ => fail(result.toString)
    }
  }

  "Abnormal case: Blank title, content, tag string" should "fail to revise" in {
    val result = useCase
      .exec(
        article = initialArticle,
        status = Private,
        title = "",
        content = "",
        comment = "second",
        editorId = AccountId("editor-id-02"),
        tags = Set("", "tag2")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: InvalidParameters =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(ArticleTitleBlank, ArticleContentBlank, ArticleTagBlank))
      case _ => fail(result.toString)
    }
  }

  "Abnormal case: the new createdAt is before the latest one" should "fail to revise" in {
    val result = useCase
      .exec(
        article = articleHasFutureCreatedAt,
        status = Private,
        title = "An updated title",
        content = "An updated content.",
        comment = "second",
        editorId = AccountId("editor-id-02"),
        tags = Set("tag1", "tag2")
      )
      .run(Applicative[Id].pure)

    result.map {
      case err: Fatal =>
        assert(err.reasons.toNonEmptyList.toList.toSet eqv Set(CreatedAtBeforeLatest))
      case _ => fail()
    }
  }

  private val mockRepository: ArticleRepository[Id] = new ArticleRepository[Id] {
    override def resolveById(id: ArticleId): Id[Option[Article]] = None
    override def store(article: Article): Id[Unit]               = ()
  }

  private val zoneId: ZoneId          = ZoneId.of("UTC")
  private val initialArticleCreatedAt = ZonedDateTime.of(2019, 7, 20, 0, 0, 0, 0, zoneId)
  private val baseInstant: Instant    = initialArticleCreatedAt.plusDays(1).toInstant
  private val clock: Clock            = Clock.fixed(baseInstant, zoneId)

  private val useCase = new ReviseArticle[Id](mockRepository, clock)

  private val initialArticle = Article
    .create(
      status = Public,
      titleResult = ArticleTitle.of("A title"),
      contentResult = ArticleContent.of("A content."),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(initialArticleCreatedAt.toInstant),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
    )
    .fold(
      e => fail(e.toString),
      a => a
    )

  private val articleHasFutureCreatedAt = Article
    .create(
      status = Public,
      titleResult = ArticleTitle.of("A title"),
      contentResult = ArticleContent.of("A content."),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(ZonedDateTime.now(zoneId).plusDays(1).toInstant),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
    )
    .fold(
      e => fail(e.toString),
      a => a
    )

}
