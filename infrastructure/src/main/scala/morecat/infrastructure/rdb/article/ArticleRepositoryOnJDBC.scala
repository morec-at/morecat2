package morecat.infrastructure.rdb.article

import morecat.domain.article._

class ArticleRepositoryOnJDBC extends ArticleRepository {

  override def resolveAll(): Seq[Article] = Seq(
    Article(ArticleId("1"), ArticleTitle("title_1"), ArticleContent("content_1")),
    Article(ArticleId("2"), ArticleTitle("title_2"), ArticleContent("content_2")),
    Article(ArticleId("3"), ArticleTitle("title_3"), ArticleContent("content_3"))
  )

}
