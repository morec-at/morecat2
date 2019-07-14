package morecat.domain.model.article

import cats.implicits._
import morecat.domain.model._

sealed abstract case class ArticleContent(value: String)

object ArticleContent {

  def of(content: String): DomainConsistencyResult[ArticleContent] =
    if (isNotBlank(content)) new ArticleContent(content) {}.validNec
    else ArticleContentBlank.invalidNec

  object ArticleContentBlank extends DomainError

}
