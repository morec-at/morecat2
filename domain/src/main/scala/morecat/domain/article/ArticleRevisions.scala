package morecat.domain.article

import java.time.{Clock, Instant}

sealed abstract case class ArticleRevisions(private val id: ArticleId, private val values: Set[ArticleRevision]) {

  val totalNumberOfRevisions: Int = values.size

  val editors: ArticleEditors = ArticleEditors(values.map(_.editor))

  def revise(editor: ArticleEditor, title: ArticleTitle, content: ArticleContent, log: ArticleUpdateLog)(
      implicit clock: Clock
  ): ArticleRevisions = {
    val newRevisionNum = values.map(_.number).max + 1
    val newRevision    = ArticleRevision(newRevisionNum, editor, title, content, log, ArticleUpdatedAt(Instant.now(clock)))
    new ArticleRevisions(id, this.values + newRevision) {}
  }

  def latest(): ArticleRevision = values.toSeq.maxBy(_.number)

  def revision(revisionNum: Int): Option[ArticleRevision] = values.find(_.number == revisionNum)

  def revisions(first: Int, second: Int): (Option[ArticleRevision], Option[ArticleRevision]) =
    (values.find(_.number == first), values.find(_.number == second))

}

object ArticleRevisions {

  def first(id: ArticleId, editor: ArticleEditor, title: ArticleTitle, content: ArticleContent, log: ArticleUpdateLog)(
      implicit clock: Clock
  ): ArticleRevisions = {
    new ArticleRevisions(id, Set(ArticleRevision(1, editor, title, content, log, ArticleUpdatedAt(Instant.now(clock))))) {}
  }

}
