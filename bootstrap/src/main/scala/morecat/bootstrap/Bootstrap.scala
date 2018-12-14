package morecat.bootstrap

import cats.effect.{Effect, IO}
import fs2.{Stream, StreamApp}
import morecat.ui.{apiVersion, HealthCheckController}
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object Bootstrap extends StreamApp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global

  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] =
    ServerStream.stream[IO]
}

object ServerStream {

  def healthCheckController[F[_]: Effect]: HttpService[F] = new HealthCheckController[F].service
//  def articleController[F[_]: Effect]: HttpService[F] =
//    new ArticleController[F](new ArticleResolver(new ArticleRepositoryOnJDBC)).service

//  val services = healthCheckController <+> articleController

  def stream[F[_]: Effect](implicit ec: ExecutionContext): Stream[IO, StreamApp.ExitCode] = {
    for {
      config <- Stream.eval(Config.load())
      exitCode <- BlazeBuilder[IO]
        .bindHttp(config.server.port, config.server.host)
        .mountService(healthCheckController, s"/$apiVersion")
//        .mountService(articleController, s"/$apiVersion")
//        .mountService(services)
        .serve
    } yield exitCode
  }

}
