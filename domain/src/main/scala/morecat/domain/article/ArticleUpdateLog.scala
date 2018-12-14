package morecat.domain.article

case class ArticleUpdateLog(value: String)

object ArticleUpdateLog {

  def noLogs(): ArticleUpdateLog = new ArticleUpdateLog("") {}

}
