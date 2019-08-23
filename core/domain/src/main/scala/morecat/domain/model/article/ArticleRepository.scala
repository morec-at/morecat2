package morecat.domain.model.article

trait ArticleRepository[F[_]] {

  def resolveById(id: ArticleId): F[Option[Article]]

  def store(article: Article): F[Unit]

}
