package morecat.usecase

import morecat.domain.article.{Article, ArticleRepository}

class ArticleResolver(repository: ArticleRepository) {

  def resolveAll(): Seq[Article] = repository.resolveAll()

}
