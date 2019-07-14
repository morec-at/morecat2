package morecat.domain.model.article

import cats.implicits._
import morecat.domain.model._
import morecat.domain.model.account.AccountId

sealed abstract case class Article(id: ArticleId,
                                   status: ArticleStatus,
                                   revisions: ArticleRevisions,
                                   tags: ArticleTags) {

  def revise(status: ArticleStatus,
             titleResult: DomainConsistencyResult[ArticleTitle],
             contentResult: DomainConsistencyResult[ArticleContent],
             editorId: AccountId,
             createdAt: ArticleRevisionCreatedAt,
             comment: ArticleRevisionComment,
             tagsResult: DomainConsistencyResult[ArticleTags]): DomainConsistencyResult[Article] = {

    val newRevisionAddedResult = this.revisions
      .add(
        titleResult = titleResult,
        contentResult = contentResult,
        editorId = editorId,
        createdAt = createdAt,
        comment = comment
      )

    (newRevisionAddedResult, tagsResult).mapN {
      case (newRevisionAdded, _tags) =>
        new Article(id = this.id, status = status, revisions = newRevisionAdded, tags = _tags) {}
    }
  }

}

object Article {

  def create(status: ArticleStatus,
             titleResult: DomainConsistencyResult[ArticleTitle],
             contentResult: DomainConsistencyResult[ArticleContent],
             editorId: AccountId,
             createdAt: ArticleRevisionCreatedAt,
             comment: ArticleRevisionComment,
             tagsResult: DomainConsistencyResult[ArticleTags]): DomainConsistencyResult[Article] = {
    val firstRevisionAddedResult = ArticleRevisions.first(
      titleResult = titleResult,
      contentResult = contentResult,
      editorId = editorId,
      createdAt = createdAt,
      comment = comment
    )
    (firstRevisionAddedResult, tagsResult).mapN {
      case (firstRevisionAdded, tags) =>
        new Article(id = ArticleId(), status = status, revisions = firstRevisionAdded, tags = tags) {}
    }
  }

}
