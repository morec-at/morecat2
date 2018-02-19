package domain.model.blog

import domain.support.Identifier

case class ArticleId(value: String) extends Identifier[String]
