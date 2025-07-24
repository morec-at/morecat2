import sbt._

object Dependencies {

  val zioVersion = "2.1.18"
  val zioHttpVersion = "3.2.0"

  val zioHttp = "dev.zio" %% "zio-http" % zioHttpVersion
  val zioTest = "dev.zio" %% "zio-test" % zioVersion % Test
  val zioTestSBT = "dev.zio" %% "zio-test-sbt" % zioVersion % Test
  val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % zioVersion % Test

  val cats = "org.typelevel" %% "cats-core" % "2.13.0"
}
