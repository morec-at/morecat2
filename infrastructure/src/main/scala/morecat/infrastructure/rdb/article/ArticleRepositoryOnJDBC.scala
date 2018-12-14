package morecat.infrastructure.rdb.article

import morecat.domain.article._

class ArticleRepositoryOnJDBC extends ArticleRepository {

  override def resolveAll(): Seq[Article] = ???

}
