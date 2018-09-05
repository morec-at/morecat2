import sbt._

object Dependencies {

  object Versions {
    val http4s    = "0.18.17"
    val specs2    = "4.2.0"
    val logback   = "1.2.3"
    val scalaTest = "3.0.5"
  }

  object Http4s {
    val blazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
    val circe       = "org.http4s" %% "http4s-circe"        % Versions.http4s
    val dsl         = "org.http4s" %% "http4s-dsl"          % Versions.http4s
  }

  object Logback {
    val classic = "ch.qos.logback" % "logback-classic" % Versions.logback
  }

  object ScalaTest {
    val scalatest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  }

}
