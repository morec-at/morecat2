package morecat.domain.article

case class ArticleRevision(number: Int,
                           editor: ArticleEditor,
                           title: ArticleTitle,
                           content: ArticleContent,
                           log: ArticleUpdateLog,
                           updatedAt: ArticleUpdatedAt)
