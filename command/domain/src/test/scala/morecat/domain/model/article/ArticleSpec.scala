package morecat.domain.model.article

import java.time.Instant

import morecat.domain.model.account.AccountId
import morecat.domain.model.article.ArticleContent.ArticleContentBlank
import morecat.domain.model.article.ArticleRevisions.CreatedAtBeforeLatest
import morecat.domain.model.article.ArticleStatus.{Private, Public}
import morecat.domain.model.article.ArticleTags.ArticleTagBlank
import morecat.domain.model.article.ArticleTitle.ArticleTitleBlank
import org.scalatest.{DiagrammedAssertions, FlatSpec}

class ArticleSpec extends FlatSpec with DiagrammedAssertions {

  behavior of "open"

  it should "open a new public article" in {
    val result = Article.create(
      status = Public,
      titleResult = ArticleTitle.of("A title"),
      contentResult = ArticleContent.of("A content."),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(Instant.now()),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
    )

    assert(result.isValid)
  }

  it should "reject it as invalid when the title is blank" in {
    val result = Article.create(
      status = Public,
      titleResult = ArticleTitle.of(""),
      contentResult = ArticleContent.of("A content."),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(Instant.now()),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
    )

    result.fold(
      invalidResult => {
        val domainErrors = invalidResult.toNonEmptyList.toList.toSet
        assert(domainErrors === Set(ArticleTitleBlank))
      },
      _ => fail()
    )
  }

  it should "reject it as invalid when the content is blank" in {
    val result = Article.create(
      status = Public,
      titleResult = ArticleTitle.of("A title"),
      contentResult = ArticleContent.of(""),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(Instant.now()),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
    )

    result.fold(
      invalidResult => {
        val domainErrors = invalidResult.toNonEmptyList.toList.toSet
        assert(domainErrors === Set(ArticleContentBlank))
      },
      _ => fail()
    )
  }

  it should "reject it as invalid when blank tags exist" in {
    val result = Article.create(
      status = Public,
      titleResult = ArticleTitle.of("A title"),
      contentResult = ArticleContent.of("A content."),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(Instant.now()),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("tag1"), ArticleTag("")))
    )

    result.fold(
      invalidResult => {
        val domainErrors = invalidResult.toNonEmptyList.toList.toSet
        assert(domainErrors === Set(ArticleTagBlank))
      },
      _ => fail()
    )
  }

  it should "reject it as invalid when the title and content are blank and exists blank tags" in {
    val result = Article.create(
      status = Public,
      titleResult = ArticleTitle.of(""),
      contentResult = ArticleContent.of(""),
      editorId = AccountId("editor-id-01"),
      createdAt = ArticleRevisionCreatedAt(Instant.now()),
      comment = ArticleRevisionComment("first"),
      tagsResult = ArticleTags.of(Set(ArticleTag("")))
    )

    result.fold(
      invalidResult => {
        val domainErrors = invalidResult.toNonEmptyList.toList.toSet
        assert(domainErrors === Set(ArticleTitleBlank, ArticleContentBlank, ArticleTagBlank))
      },
      _ => fail()
    )
  }

  behavior of "revise"

  it should "revise an existing article" in {
    val initialArticle = Article
      .create(
        status = Public,
        titleResult = ArticleTitle.of("A title"),
        contentResult = ArticleContent.of("A content."),
        editorId = AccountId("editor-id-01"),
        createdAt = ArticleRevisionCreatedAt(Instant.now()),
        comment = ArticleRevisionComment("first"),
        tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
      )
      .fold(
        _ => fail(),
        a => a
      )

    val revisedArticle = initialArticle
      .revise(
        status = Private,
        titleResult = ArticleTitle.of("An updated title"),
        contentResult = ArticleContent.of("An updated content."),
        editorId = AccountId("editor-id-02"),
        createdAt = ArticleRevisionCreatedAt(Instant.now()),
        comment = ArticleRevisionComment("second"),
        tagsResult = ArticleTags.of(Set(ArticleTag("tag1"), ArticleTag("tag2")))
      )
      .fold(
        _ => fail(),
        a => a
      )

    assert(initialArticle.id === revisedArticle.id)
  }

  it should "reject it as invalid when the createdAt is before the latest one" in {
    val oldTime = ArticleRevisionCreatedAt(Instant.ofEpochMilli(1000000000000L))
    val newTime = ArticleRevisionCreatedAt(Instant.ofEpochMilli(1000000000001L))

    val initialArticle = Article
      .create(
        status = Public,
        titleResult = ArticleTitle.of("A title"),
        contentResult = ArticleContent.of("A content."),
        editorId = AccountId("editor-id-01"),
        createdAt = newTime,
        comment = ArticleRevisionComment("first"),
        tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
      )
      .fold(
        _ => fail(),
        a => a
      )

    val revisedArticleResult = initialArticle
      .revise(
        status = Private,
        titleResult = ArticleTitle.of("An updated title"),
        contentResult = ArticleContent.of("An updated content."),
        editorId = AccountId("editor-id-02"),
        createdAt = oldTime,
        comment = ArticleRevisionComment("second"),
        tagsResult = ArticleTags.of(Set(ArticleTag("tag1"), ArticleTag("tag2")))
      )

    revisedArticleResult.fold(
      invalidResult => {
        val domainErrors = invalidResult.toNonEmptyList.toList.toSet
        assert(domainErrors === Set(CreatedAtBeforeLatest))
      },
      _ => fail()
    )
  }

  it should "reject it as invalid when the createdAt is the same the latest one" in {
    val now = ArticleRevisionCreatedAt(Instant.ofEpochMilli(1000000000000L))

    val initialArticle = Article
      .create(
        status = Public,
        titleResult = ArticleTitle.of("A title"),
        contentResult = ArticleContent.of("A content."),
        editorId = AccountId("editor-id-01"),
        createdAt = now,
        comment = ArticleRevisionComment("first"),
        tagsResult = ArticleTags.of(Set(ArticleTag("tag1")))
      )
      .fold(
        _ => fail(),
        a => a
      )

    val revisedArticleResult = initialArticle
      .revise(
        status = Private,
        titleResult = ArticleTitle.of("An updated title"),
        contentResult = ArticleContent.of("An updated content."),
        editorId = AccountId("editor-id-02"),
        createdAt = now,
        comment = ArticleRevisionComment("second"),
        tagsResult = ArticleTags.of(Set(ArticleTag("tag1"), ArticleTag("tag2")))
      )

    revisedArticleResult.fold(
      invalidResult => {
        val domainErrors = invalidResult.toNonEmptyList.toList.toSet
        assert(domainErrors === Set(CreatedAtBeforeLatest))
      },
      _ => fail()
    )
  }

}
