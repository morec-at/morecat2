package morecat.domain.article

trait ArticleRepository {

  def resolveAll(): Seq[Article]

}
