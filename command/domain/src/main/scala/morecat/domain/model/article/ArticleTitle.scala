package morecat.domain.model.article

import cats.implicits._
import morecat.domain.model._

sealed abstract case class ArticleTitle(value: String)

object ArticleTitle {

  def of(title: String): DomainConsistencyResult[ArticleTitle] =
    if (isNotBlank(title)) new ArticleTitle(title) {}.validNec
    else ArticleTitleBlank.invalidNec

  object ArticleTitleBlank extends DomainError

}
