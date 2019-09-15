package morecat.domain.model.article

import java.time.Instant

final case class ArticleRevisionCreatedAt(value: Instant)

object ArticleRevisionCreatedAt {

  implicit val articleRevisionCreatedAtOrdering: Ordering[ArticleRevisionCreatedAt] =
    (x: ArticleRevisionCreatedAt, y: ArticleRevisionCreatedAt) => x.value.compareTo(y.value)

}
