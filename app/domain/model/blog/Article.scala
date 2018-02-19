package domain.model.blog

import domain.support.Entity

case class Article(id: ArticleId,
                   title: ArticleTitle,
                   content: ArticleContent,
                   createdDateTime: ArticleCreatedDateTime,
                   lastUpdatedDateTime: ArticleLastUpdatedDateTime)
    extends Entity[ArticleId]
