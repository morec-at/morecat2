package domain.model.blog

import java.time.LocalDateTime

import domain.support.ValueObject

case class ArticleCreatedDateTime(value: LocalDateTime) extends ValueObject[LocalDateTime]
