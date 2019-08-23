package morecat.usecase.article

import java.time.{Clock, Instant}

import cats.Monad
import cats.data.NonEmptyChain
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import morecat.domain.model.DomainError
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleContent.ArticleContentBlank
import morecat.domain.model.article.ArticleRevisions.CreatedAtBeforeLatest
import morecat.domain.model.article.ArticleTags.ArticleTagBlank
import morecat.domain.model.article.ArticleTitle.ArticleTitleBlank
import morecat.domain.model.article._
import morecat.usecase._

final class ReviseArticle[F[_]: Monad](repository: ArticleRepository[F], clock: Clock) {

  import ReviseArticle._

  def exec(article: Article,
           status: ArticleStatus,
           title: String,
           content: String,
           comment: String,
           editorId: AccountId,
           tags: Set[String]): UseCaseCont[F, UseCaseResult] =
    UseCaseCont { f =>
      article.revise(
        status = status,
        titleResult = ArticleTitle.of(title),
        contentResult = ArticleContent.of(content),
        editorId = editorId,
        createdAt = ArticleRevisionCreatedAt(Instant.now(clock)),
        comment = ArticleRevisionComment(comment),
        tagsResult = ArticleTags.of(tags.map(ArticleTag))
      ) match {
        case Valid(revisedArticle) => repository.store(revisedArticle).flatMap(_ => f(ArticleRevised(revisedArticle)))
        case Invalid(domainErrors) => Monad[F].point(domainErrors)
      }
    }

}

object ReviseArticle {

  final case class ArticleRevised(data: Article) extends NormalCase

  private implicit def toUseCaseResult(domainErrors: NonEmptyChain[DomainError]): UseCaseResult = {
    val errors = domainErrors.toNonEmptyList.toList.toSet
    errors match {
      case _ if errors.contains(CreatedAtBeforeLatest) =>
        Fatal(domainErrors)
      case _ if errors.exists(Set(ArticleTitleBlank, ArticleContentBlank, ArticleTagBlank).contains) =>
        InvalidParameters(domainErrors)
      case _ =>
        Fatal(domainErrors)
    }
  }

}
