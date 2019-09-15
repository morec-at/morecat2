package morecat.domain.model.article

import cats.data.NonEmptyList
import cats.implicits._
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleRevisions.CreatedAtBeforeLatest
import morecat.domain.model.{DomainConsistencyResult, DomainError}

sealed abstract case class ArticleRevisions(breachEncapsulationOfValues: NonEmptyList[ArticleRevision]) {

  val latest: ArticleRevision = this.breachEncapsulationOfValues.toList.maxBy(_.createdAt)

  val size: Int = this.breachEncapsulationOfValues.size

  def add(titleResult: DomainConsistencyResult[ArticleTitle],
          contentResult: DomainConsistencyResult[ArticleContent],
          editorId: AccountId,
          createdAt: ArticleRevisionCreatedAt,
          comment: ArticleRevisionComment): DomainConsistencyResult[ArticleRevisions] = {

    (titleResult, contentResult, checkCreatedAt(createdAt)).mapN {
      case (title, content, _createdAt) =>
        val newRevision = ArticleRevision.of(
          title = title,
          content = content,
          editorId = editorId,
          createdAt = _createdAt,
          comment = comment
        )

        new ArticleRevisions(NonEmptyList.of(newRevision).concatNel(this.breachEncapsulationOfValues)) {}
    }
  }

  private def checkCreatedAt(
      newRevisionCreatedAt: ArticleRevisionCreatedAt
  ): DomainConsistencyResult[ArticleRevisionCreatedAt] =
    if (newRevisionCreatedAt.value.isAfter(latest.createdAt.value)) newRevisionCreatedAt.validNec
    else CreatedAtBeforeLatest.invalidNec
}

object ArticleRevisions {

  def first(titleResult: DomainConsistencyResult[ArticleTitle],
            contentResult: DomainConsistencyResult[ArticleContent],
            editorId: AccountId,
            createdAt: ArticleRevisionCreatedAt,
            comment: ArticleRevisionComment): DomainConsistencyResult[ArticleRevisions] =
    (titleResult, contentResult).mapN {
      case (title, content) =>
        val firstRevision = ArticleRevision.of(
          title = title,
          content = content,
          editorId = editorId,
          createdAt = createdAt,
          comment = comment
        )

        new ArticleRevisions(NonEmptyList.of(firstRevision)) {}
    }

  object CreatedAtBeforeLatest extends DomainError

}
