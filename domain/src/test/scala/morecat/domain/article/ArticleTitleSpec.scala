package morecat.domain.article

import org.scalatest.{DiagrammedAssertions, FlatSpec}

class ArticleTitleSpec extends FlatSpec with DiagrammedAssertions {

  behavior of "ArticleTitle"

  it should "provide the pretty version for url" in {
    val spaces = "this is title"
    assert(ArticleTitle(spaces).prettifiedForUrl === "this-is-title")

    val spacesAndBlank = " this is title  "
    assert(ArticleTitle(spacesAndBlank).prettifiedForUrl === "this-is-title")

    val nonAscii = "　これは タイトル　です "
    assert(ArticleTitle(nonAscii).prettifiedForUrl === "これは-タイトル-です")
  }

  it should "provide the shortened one" in {
    val longTitle = ArticleTitle("looooooooooooooooooooooooooooong title")
    assert(longTitle.shortened === "looooooooooooooooooooooooooooong...")
  }

  it should "not be empty" in {
    assertThrows[IllegalArgumentException](ArticleTitle(""))
    assertThrows[IllegalArgumentException](ArticleTitle(" "))
    assertThrows[IllegalArgumentException](ArticleTitle("　"))
  }

}
