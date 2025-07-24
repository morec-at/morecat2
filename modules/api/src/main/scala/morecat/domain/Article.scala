package morecat.domain

import cats.data.*
import cats.syntax.all.*
import morecat.domain.Article.validateBody
import morecat.domain.Article.validateTitle

sealed trait Article {
  val id: Long
  val title: String
  val body: String
}
object Article {
  def validateTitle(title: String): ArticleValidationResult[String] =
    if (title.isBlank()) TitleRequired.invalidNec else title.validNec
  def validateBody(body: String): ArticleValidationResult[String] =
    if (body.isBlank()) BodyRequired.invalidNec else body.validNec
}

sealed abstract class Draft(
    override val id: Long,
    override val title: String,
    override val body: String
) extends Article {
  def publish(): Published =
    new Published(id = id, title = title, body = body) {}
}
object Draft {
  def create(
      id: Long,
      title: String,
      body: String
  ): ArticleValidationResult[Draft] =
    (
      validateTitle(title),
      validateBody(body)
    ).mapN { case (validTitle, validBody) =>
      new Draft(id = id, title = validTitle, body = validBody) {}
    }
}

sealed abstract class Published(
    override val id: Long,
    override val title: String,
    override val body: String
) extends Article {
  def archive: Archived =
    new Archived(id = id, title = title, body = body) {}
}

sealed abstract class Archived(
    override val id: Long,
    override val title: String,
    override val body: String
) extends Article {
  def publish(): Published =
    new Published(id = id, title = title, body = body) {}
}

sealed trait ArticleValidation
case object TitleRequired extends ArticleValidation
case object BodyRequired extends ArticleValidation

type ArticleValidationResult[A] = ValidatedNec[ArticleValidation, A]
