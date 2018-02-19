package domain.model.blog

import domain.support.ValueObject

case class ArticleTitle(value: String) extends ValueObject[String]
