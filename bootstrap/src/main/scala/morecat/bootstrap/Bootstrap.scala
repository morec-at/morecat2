package morecat.bootstrap

import cats.effect.{Effect, IO}
import fs2.StreamApp
import morecat.ui.apiVersion
import morecat.ui.HealthCheckController
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object Bootstrap extends StreamApp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global

  def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] =
    ServerStream.stream[IO]
}

object ServerStream {

  def helloWorldService[F[_]: Effect]: HttpService[F] = new HealthCheckController[F].service

  def stream[F[_]: Effect](implicit ec: ExecutionContext): fs2.Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .mountService(helloWorldService, s"/$apiVersion")
      .serve

}
