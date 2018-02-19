package application.blog

import javax.inject.{Inject, Singleton}

import domain.lifecycle.blog.ArticleRepository
import domain.model.blog.Article
import domain.support.IOContext

import scala.concurrent.Future

@Singleton
class ArticleResolveService @Inject()(repository: ArticleRepository)(implicit private val iOContext: IOContext) {

  def resolveAll(): Future[Seq[Article]] = repository.resolveAll

}
