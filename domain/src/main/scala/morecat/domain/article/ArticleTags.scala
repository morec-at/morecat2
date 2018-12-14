package morecat.domain.article

abstract sealed case class ArticleTags(private val values: Set[ArticleTag]) {

  val all: Set[ArticleTag] = values

}

object ArticleTags {

  val noTags: ArticleTags = new ArticleTags(Set.empty) {}

  def of(tags: String*): ArticleTags = new ArticleTags(tags.map(ArticleTag).toSet) {}

}
