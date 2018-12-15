package morecat.ui

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HealthCheckRoutes[F[_]: Sync] extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case GET -> Root / "healthz" =>
        Ok("ok")
    }
  }

}
