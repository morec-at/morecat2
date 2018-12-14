package morecat.domain.article

case class ArticleContent(value: String) {

  require(isNotBlank(value), "Content must not be empty")

}
