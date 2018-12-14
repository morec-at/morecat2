package morecat.domain.article

case class ArticleTag(value: String) {

  require(isNotBlank(value), "Tag  must not be empty")

}
