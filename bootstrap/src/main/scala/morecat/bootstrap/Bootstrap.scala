package morecat.bootstrap

import cats.effect._
import cats.implicits._
import morecat.ui._
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Bootstrap extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    ServerStream.stream[IO].compile.drain.as(ExitCode.Success)

}

object ServerStream {

  def stream[F[_]: ConcurrentEffect: Timer]: fs2.Stream[F, ExitCode] = {
    def healthCheckRoutes: HttpRoutes[F] = new HealthCheckRoutes[F].routes

    val routes = healthCheckRoutes

    val httpApp = Router(s"/$apiVersion" -> routes).orNotFound

    for {
//      config <- fs2.Stream.eval(Config.load())
      exitCode <- BlazeServerBuilder[F]
//        .bindHttp(config.server.port, config.server.host)
        .withHttpApp(httpApp)
        .serve
    } yield exitCode
  }

}
