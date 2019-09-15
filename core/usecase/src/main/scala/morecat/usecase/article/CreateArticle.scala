package morecat.usecase.article

import java.time.{Clock, Instant}

import cats.Monad
import cats.data.NonEmptyChain
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import morecat.domain.model.DomainError
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleContent.ArticleContentBlank
import morecat.domain.model.article.ArticleTags.ArticleTagBlank
import morecat.domain.model.article.ArticleTitle.ArticleTitleBlank
import morecat.domain.model.article._
import morecat.usecase._

final class CreateArticle[F[_]: Monad](repository: ArticleRepository[F], clock: Clock) {

  import CreateArticle._

  def exec(status: ArticleStatus,
           title: String,
           content: String,
           comment: String,
           editorId: AccountId,
           tags: Set[String]): UseCaseCont[F, UseCaseResult] =
    UseCaseCont { f =>
      Article.create(
        status = status,
        titleResult = ArticleTitle.of(title),
        contentResult = ArticleContent.of(content),
        editorId = editorId,
        createdAt = ArticleRevisionCreatedAt(Instant.now(clock)),
        comment = ArticleRevisionComment(comment),
        tagsResult = ArticleTags.of(tags.map(ArticleTag))
      ) match {
        case Valid(newArticle)     => repository.store(newArticle).flatMap(_ => f(ArticleCreated(newArticle)))
        case Invalid(domainErrors) => Monad[F].point(domainErrors)
      }
    }

}

object CreateArticle {

  final case class ArticleCreated(data: Article) extends NormalCase

  private implicit def toUseCaseResult(domainErrors: NonEmptyChain[DomainError]): UseCaseResult = {
    val errors = domainErrors.toNonEmptyList.toList.toSet
    errors match {
      case _ if errors.exists(Set(ArticleTitleBlank, ArticleContentBlank, ArticleTagBlank).contains) =>
        InvalidParameters(domainErrors)
      case _ =>
        Fatal(domainErrors)
    }
  }

}
