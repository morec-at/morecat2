package domain.model.blog

import domain.support.ValueObject

case class ArticleContent(value: String) extends ValueObject[String]
