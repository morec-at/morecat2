package morecat.domain

import zio.test.*
import cats.data.*

object ArticleSpec extends ZIOSpecDefault {
  def spec = suite("ArticleSpec")(
    suite("Draft#create")(
      test("should create a Draft when title and body are valid") {
        val result = Draft.create(1L, "Valid Title", "Valid body content")
        assertTrue(result.isValid) &&
        assertTrue(result.toOption.get.id == 1L) &&
        assertTrue(result.toOption.get.title == "Valid Title") &&
        assertTrue(result.toOption.get.body == "Valid body content")
      },
      test("should fail when title is blank") {
        val result = Draft.create(1L, "", "Valid body content")
        assertTrue(result.isInvalid) &&
        assertTrue(
          result.swap.toOption.get.toChain.toList.contains(TitleRequired)
        )
      },
      test("should fail when body is blank") {
        val result = Draft.create(1L, "Valid Title", "")
        assertTrue(result.isInvalid) &&
        assertTrue(
          result.swap.toOption.get.toChain.toList.contains(BodyRequired)
        )
      },
      test("should fail when both title and body are blank") {
        val result = Draft.create(1L, "", "")
        assertTrue(result.isInvalid) &&
        assertTrue(
          result.swap.toOption.get.toChain.toList.contains(TitleRequired)
        ) &&
        assertTrue(
          result.swap.toOption.get.toChain.toList.contains(BodyRequired)
        )
      },
      test("should fail when title is only whitespace") {
        val result = Draft.create(1L, "   ", "Valid body content")
        assertTrue(result.isInvalid) &&
        assertTrue(
          result.swap.toOption.get.toChain.toList.contains(TitleRequired)
        )
      },
      test("should fail when body is only whitespace") {
        val result = Draft.create(1L, "Valid Title", "   ")

        assertTrue(result.isInvalid) &&
        assertTrue(
          result.swap.toOption.get.toChain.toList.contains(BodyRequired)
        )
      }
    )
  )
}
