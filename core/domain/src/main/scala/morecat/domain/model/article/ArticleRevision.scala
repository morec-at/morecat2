package morecat.domain.model.article

import morecat.domain.model.account.AccountId

sealed abstract case class ArticleRevision(title: ArticleTitle,
                                           content: ArticleContent,
                                           editorId: AccountId,
                                           createdAt: ArticleRevisionCreatedAt,
                                           comment: ArticleRevisionComment)

object ArticleRevision {

  def of(title: ArticleTitle,
         content: ArticleContent,
         editorId: AccountId,
         createdAt: ArticleRevisionCreatedAt,
         comment: ArticleRevisionComment): ArticleRevision =
    new ArticleRevision(
      title = title,
      content = content,
      editorId = editorId,
      createdAt = createdAt,
      comment = comment
    ) {}

}
