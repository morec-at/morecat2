package morecat.domain.article

case class ArticleTitle(value: String) {

  require(isNotBlank(value), "Title must not be empty")

  private val Space          = '\u0020'
  private val NoBreakSpace   = '\u00A0'
  private val FullWidthSpace = '\u3000'

  private val ShortenedThreshold = 32

  val prettifiedForUrl: String = this
    .trim()
    .replace(Space, '-')

  val shortened: String = if (value.length > ShortenedThreshold) {
    value.take(ShortenedThreshold) + "..."
  } else value

  private def trim(): String = {
    this.value
      .replace(NoBreakSpace, Space)
      .replace(FullWidthSpace, Space)
      .trim
  }

}
