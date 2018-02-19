package port.secondary.persistence.rdb.blog

import javax.inject.{Inject, Singleton}

import domain.lifecycle.blog.ArticleRepository
import domain.model.blog._
import domain.support.IOContext
import port.secondary.persistence.rdb.RepositoryOnJDBC
import port.secondary.persistence.rdb.support.ExecutionContextOnJdbc
import scalikejdbc._

import scala.concurrent.Future

@Singleton
class ArticleRepositoryOnJDBC @Inject()(implicit val ec: ExecutionContextOnJdbc)
    extends ArticleRepository
    with RepositoryOnJDBC {

  private val a = ArticleRecord.a

  override def resolveAll()(implicit ctx: IOContext): Future[Seq[Article]] = Future {
    withDBSession(ctx) { implicit s =>
      withSQL {
        selectFrom(ArticleRecord as a)
      }.map(ArticleRecord(a)).list.apply.map(toEntity)
    }
  }

  private def toEntity(record: ArticleRecord): Article =
    Article(
      id = ArticleId(record.articleId),
      title = ArticleTitle(record.title),
      content = ArticleContent(record.content),
      createdDateTime = ArticleCreatedDateTime(record.createdDateTime),
      lastUpdatedDateTime = ArticleLastUpdatedDateTime(record.lastUpdatedDateTime)
    )

}
