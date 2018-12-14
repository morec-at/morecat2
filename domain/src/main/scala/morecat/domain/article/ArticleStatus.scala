package morecat.domain.article

sealed abstract class ArticleStatus

case object Public  extends ArticleStatus
case object Private extends ArticleStatus
