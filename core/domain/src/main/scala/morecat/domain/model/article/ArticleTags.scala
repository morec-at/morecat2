package morecat.domain.model.article

import cats.implicits._
import morecat.domain.model._

sealed abstract case class ArticleTags(breachEncapsulationOfValues: Set[ArticleTag])

object ArticleTags {

  def of(tags: Set[ArticleTag]): DomainConsistencyResult[ArticleTags] = {
    if (tags.forall(tag => isNotBlank(tag.value))) new ArticleTags(tags) {}.validNec
    else ArticleTagBlank.invalidNec
  }

  object ArticleTagBlank extends DomainError

}
