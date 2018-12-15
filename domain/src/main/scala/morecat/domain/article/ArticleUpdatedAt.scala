package morecat.domain.article

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

case class ArticleUpdatedAt(value: Instant) {

  // FIXME injection
  private val zoneId = ZoneId.of("UTC")

  private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(zoneId)

  val formattedForUrl: String = formatter.format(value)

}
