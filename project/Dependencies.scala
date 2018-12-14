import sbt._

object Dependencies {

  object Versions {
    val http4s     = "0.18.17"
    val doobie     = "0.5.3"
    val airframe   = "0.61"
    val logback    = "1.2.3"
    val pureConfig = "0.9.2"
    val scalaTest  = "3.0.5"
  }

  object Http4s {
    val blazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
    val circe       = "org.http4s" %% "http4s-circe"        % Versions.http4s
    val dsl         = "org.http4s" %% "http4s-dsl"          % Versions.http4s
  }

  object Doobie {
    val doobie = "org.tpolecat" %% "doobie-postgres" % Versions.doobie
  }

  object Airframe {
    val di = "org.wvlet.airframe" %% "airframe" % Versions.airframe
  }

  object Logback {
    val classic = "ch.qos.logback" % "logback-classic" % Versions.logback
  }

  object PureConfig {
    val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfig
  }

  object ScalaTest {
    val scalatest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  }

}
