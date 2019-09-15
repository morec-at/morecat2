package morecat.query.article

object PublicArticleResolver {

  def all: Seq[Article] = Seq(
    Article("1"),
    Article("2"),
    Article("3")
  )

}
