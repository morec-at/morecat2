package morecat.ui

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{DiagrammedAssertions, FlatSpec}

class HealthCheckControllerSpec extends FlatSpec with DiagrammedAssertions {

  behavior of "HealthCheckController"

  it should "return 200" in {
    assert(healthCheckResponse.status === Status.Ok)
  }

  it should "return ok" in {
    assert(healthCheckResponse.as[String].unsafeRunSync() === "ok")
  }

  private[this] val healthCheckResponse: Response[IO] = {
    val getHealtz = Request[IO](Method.GET, Uri.uri("/healthz"))
    new HealthCheckController[IO].service.orNotFound(getHealtz).unsafeRunSync()
  }

}
