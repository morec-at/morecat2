package port.secondary.persistence.rdb.blog

import java.time.LocalDateTime

import scalikejdbc._

case class ArticleRecord(articleId: String,
                         title: String,
                         content: String,
                         createdDateTime: LocalDateTime,
                         lastUpdatedDateTime: LocalDateTime)

object ArticleRecord extends SQLSyntaxSupport[ArticleRecord] {

  override val tableName = "articles"

  val a = syntax("a")

  def apply(a: ResultName[ArticleRecord])(rs: WrappedResultSet): ArticleRecord =
    ArticleRecord(
      articleId = rs.get(a.articleId),
      title = rs.get(a.title),
      content = rs.get(a.content),
      createdDateTime = rs.get(a.createdDateTime),
      lastUpdatedDateTime = rs.get(a.lastUpdatedDateTime)
    )

  def apply(a: SyntaxProvider[ArticleRecord])(rs: WrappedResultSet): ArticleRecord =
    apply(a.resultName)(rs)

}
