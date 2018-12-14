package morecat.ui.article

import cats.effect.Effect
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class ArticleController[F[_]: Effect]() extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root / "articles" =>
        NotImplemented()
    }
  }

}
