package morecat.domain.article

import java.time.{Clock, ZoneId, ZonedDateTime}

import org.scalatest.{DiagrammedAssertions, FlatSpec}

class ArticleSpec extends FlatSpec with DiagrammedAssertions {

  implicit val clock: Clock = Clock.systemDefaultZone()

  behavior of "Article"

  it should "create a new Article with first revision" in {
    val id      = ArticleId("1")
    val editor  = ArticleEditor("editor_1")
    val title   = ArticleTitle("title_1")
    val content = ArticleContent("content_1")
    val log     = ArticleUpdateLog.noLogs()

    val newArticle = Article.create(
      id = id,
      tags = ArticleTags.noTags,
      editor = editor,
      title = title,
      content = content,
      status = Public,
      log = log
    )

    assert(newArticle.id === id)
    assert(newArticle.currentStatus === Public)

    val latestRevision = newArticle.latest
    assert(latestRevision.number === 1)
    assert(latestRevision.editor === editor)
    assert(latestRevision.title === title)
    assert(latestRevision.content === content)
    assert(latestRevision.log === log)
  }

  it should "update an existing Article" in {
    val initial = Article.create(
      id = ArticleId("1"),
      tags = ArticleTags.noTags,
      editor = ArticleEditor("editor_1"),
      title = ArticleTitle("initial_title"),
      content = ArticleContent("initial_content"),
      status = Public,
      log = ArticleUpdateLog("initial")
    )

    val secondEditor   = ArticleEditor("editor_2")
    val updatedTitle   = ArticleTitle("updated_title")
    val updatedContent = ArticleContent("updated_content")
    val secondLog      = ArticleUpdateLog("updated")

    val updated = initial.update(secondEditor, updatedTitle, updatedContent, secondLog)

    assert(updated.totalNumberOfRevisions === 2)

    val latestRevision = updated.latest
    assert(latestRevision.number === 2)
    assert(latestRevision.editor === secondEditor)
    assert(latestRevision.title === updatedTitle)
    assert(latestRevision.content === updatedContent)
    assert(latestRevision.log === secondLog)
  }

  it should "get a specific revision" in {
    val initial = Article.create(
      id = ArticleId("1"),
      tags = ArticleTags.noTags,
      editor = ArticleEditor("editor_1"),
      title = ArticleTitle("initial_title"),
      content = ArticleContent("initial_content"),
      status = Public,
      log = ArticleUpdateLog("initial")
    )

    val updated =
      initial.update(ArticleEditor("editor_2"),
                     ArticleTitle("updated_title"),
                     ArticleContent("updated_content"),
                     ArticleUpdateLog("updated"))

    assert(updated.revision(1).isDefined)
    assert(updated.revision(2).isDefined)
  }

  it should "get two specific revisions" in {
    val initial = Article.create(
      id = ArticleId("1"),
      tags = ArticleTags.noTags,
      editor = ArticleEditor("editor_1"),
      title = ArticleTitle("initial_title"),
      content = ArticleContent("initial_content"),
      status = Public,
      log = ArticleUpdateLog("initial")
    )

    val updated =
      initial.update(ArticleEditor("editor2"),
                     ArticleTitle("updated_title"),
                     ArticleContent("updated_content"),
                     ArticleUpdateLog("updated"))

    val (maybeOne, maybeTwo) = updated.revisions(1, 2)
    assert(maybeOne.isDefined)
    assert(maybeTwo.isDefined)
  }

  it should "change the status" in {
    val initial = Article.create(
      id = ArticleId("1"),
      tags = ArticleTags.noTags,
      editor = ArticleEditor("editor"),
      title = ArticleTitle("title"),
      content = ArticleContent("content"),
      status = Public,
      log = ArticleUpdateLog.noLogs()
    )

    val statusChanged = initial.changeStatus(Private)
    assert(statusChanged.currentStatus == Private)
  }

  it should "set tags" in {
    val initial = Article.create(
      id = ArticleId("1"),
      tags = ArticleTags.noTags,
      editor = ArticleEditor("editor"),
      title = ArticleTitle("title"),
      content = ArticleContent("content"),
      status = Public,
      log = ArticleUpdateLog.noLogs()
    )

    val tagged = initial.tagging("A", "B", "C")
    assert(tagged.tags.all.size === 3)
  }

  it should "have a URL" in {
    val zoneId    = ZoneId.of("UTC")
    val createdAt = ZonedDateTime.of(2019, 1, 1, 0, 0, 0, 0, zoneId).toInstant

    val initial = Article.create(
      id = ArticleId("1"),
      tags = ArticleTags.noTags,
      editor = ArticleEditor("editor"),
      title = ArticleTitle("This is title"),
      content = ArticleContent("content"),
      status = Public,
      log = ArticleUpdateLog.noLogs()
    )(Clock.fixed(createdAt, zoneId))

    assert(initial.url() === "2019/01/01/this-is-title")
    assert(initial.url(Some("you-can-override-it")) === "2019/01/01/you-can-override-it")

    val updatedAt = ZonedDateTime.of(2019, 2, 1, 0, 0, 0, 0, zoneId).toInstant

    val updated = initial.update(
      editor = ArticleEditor("editor"),
      title = ArticleTitle("This is title(edited)"),
      content = ArticleContent("updated content"),
      log = ArticleUpdateLog.noLogs()
    )(Clock.fixed(updatedAt, zoneId))

    println(updated.url())
    assert(updated.url() === "2019/01/01/this-is-title(edited)")
  }

}
