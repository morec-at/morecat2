package domain.lifecycle.blog

import domain.model.blog.Article
import domain.support.IOContext

import scala.concurrent.Future

trait ArticleRepository {

  def resolveAll()(implicit ctx: IOContext): Future[Seq[Article]]

}
