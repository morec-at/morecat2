package morecat.domain.model.article

import cats.Monoid
import cats.implicits._
import morecat.domain.model.{DomainConsistencyResult, DomainError}
import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleRevisions.{CreatedAtBeforeLatest, NoRevisions}

sealed abstract case class ArticleRevisions(breachEncapsulationOfValues: Set[ArticleRevision]) {

  val latest: Option[ArticleRevision] =
    if (this.breachEncapsulationOfValues.nonEmpty) Some(this.breachEncapsulationOfValues.toSeq.maxBy(_.createdAt))
    else None

  def add(titleResult: DomainConsistencyResult[ArticleTitle],
          contentResult: DomainConsistencyResult[ArticleContent],
          editorId: AccountId,
          createdAt: ArticleRevisionCreatedAt,
          comment: ArticleRevisionComment): DomainConsistencyResult[ArticleRevisions] = {
    val createdAtResult = latest match {
      case Some(latestRev) =>
        if (createdAt.value.isAfter(latestRev.createdAt.value)) createdAt.validNec
        else CreatedAtBeforeLatest.invalidNec

      case None => NoRevisions.invalidNec
    }

    (titleResult, contentResult, createdAtResult).mapN {
      case (title, content, _createdAt) =>
        val newRevision = ArticleRevision.of(
          title = title,
          content = content,
          editorId = editorId,
          createdAt = _createdAt,
          comment = comment
        )

        this |+| new ArticleRevisions(Set(newRevision)) {}
    }
  }

}

object ArticleRevisions {

  private val empty: ArticleRevisions = new ArticleRevisions(Set.empty) {}

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

        empty |+| new ArticleRevisions(Set(firstRevision)) {}
    }

  object NoRevisions           extends DomainError
  object CreatedAtBeforeLatest extends DomainError

  implicit val articleRevisionsMonoid: Monoid[ArticleRevisions] = new Monoid[ArticleRevisions] {
    override def empty: ArticleRevisions = new ArticleRevisions(Set.empty) {}
    override def combine(x: ArticleRevisions, y: ArticleRevisions): ArticleRevisions =
      new ArticleRevisions(x.breachEncapsulationOfValues ++ y.breachEncapsulationOfValues) {}
  }

}
