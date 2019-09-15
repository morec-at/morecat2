package morecat.domain.model.article

sealed trait ArticleStatus

object ArticleStatus {

  object Public  extends ArticleStatus
  object Private extends ArticleStatus

}
