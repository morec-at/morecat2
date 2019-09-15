package morecat.usecase.article

import cats.Monad
import cats.implicits._
import morecat.domain.model.article.{Article, ArticleId, ArticleRepository}
import morecat.usecase._

final class ResolveArticle[F[_]: Monad](repository: ArticleRepository[F]) {

  import ResolveArticle._

  def exec(id: ArticleId): UseCaseCont[F, UseCaseResult] = UseCaseCont { f =>
    repository.resolveById(id).flatMap {
      case Some(article) => f(ArticleResolved(article))
      case None          => Monad[F].point(ArticleNotFound)
    }
  }

}

object ResolveArticle {

  final case class ArticleResolved(data: Article) extends NormalCase
  object ArticleNotFound                          extends AbnormalCase

}
