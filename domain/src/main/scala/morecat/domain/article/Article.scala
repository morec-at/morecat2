package morecat.domain.article

import java.time.Clock

sealed abstract case class Article(id: ArticleId,
                                   tags: ArticleTags,
                                   private val status: ArticleStatus,
                                   private val revisions: ArticleRevisions) {

  val latest: ArticleRevision = revisions.latest()

  val totalNumberOfRevisions: Int = revisions.totalNumberOfRevisions

  val currentStatus: ArticleStatus = status

  val editors: ArticleEditors = revisions.editors

  def revision(revisionNum: Int): Option[ArticleRevision] = revisions.revision(revisionNum)

  def revisions(first: Int, second: Int): (Option[ArticleRevision], Option[ArticleRevision]) =
    revisions.revisions(first, second)

  def update(editor: ArticleEditor, title: ArticleTitle, content: ArticleContent, log: ArticleUpdateLog)(
      implicit clock: Clock
  ): Article = {
    val newRevisions = revisions.revise(editor, title, content, log)
    new Article(id = this.id, status = this.status, tags = this.tags, revisions = newRevisions) {}
  }

  def changeStatus(status: ArticleStatus): Article =
    new Article(id = this.id, status = status, tags = this.tags, revisions = this.revisions) {}

  def tagging(tags: String*): Article =
    new Article(id = this.id, status = this.status, tags = ArticleTags.of(tags: _*), revisions = this.revisions) {}

}

object Article {

  def create(id: ArticleId,
             tags: ArticleTags,
             title: ArticleTitle,
             content: ArticleContent,
             status: ArticleStatus,
             editor: ArticleEditor,
             log: ArticleUpdateLog)(
      implicit clock: Clock
  ): Article =
    new Article(id = id,
                status = status,
                tags = tags,
                revisions = ArticleRevisions.first(id, editor, title, content, log)) {}

}
