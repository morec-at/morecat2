package morecat.ui.article

import cats.effect.Effect
import morecat.usecase.ArticleResolver
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class ArticleController[F[_]: Effect](articleResolver: ArticleResolver) extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root / "articles" =>
//        Ok(articleResolver.resolveAll())
        Ok(articleResolver.resolveAll().head.content.value)
    }
  }

}
