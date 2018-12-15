package morecat.domain.article

case class ArticleEditor(value: String) {

  require(isNotBlank(value), "Author must have a value represents itself like a name")

}
