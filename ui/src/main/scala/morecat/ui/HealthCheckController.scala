package morecat.ui

import cats.effect.Effect
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class HealthCheckController[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root / "healthz" =>
        Ok("ok")
    }
  }

}
