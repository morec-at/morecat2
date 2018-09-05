import Dependencies._

inThisBuild(
  Seq(
    organization := "morecat",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
    scalacOptions ++= ScalacOptions.enabled
  )
)

lazy val commonSettings = Seq(
  scalacOptions in (Compile, console) --= ScalacOptions.disabledInRepl,
  libraryDependencies ++= Seq(
    Logback.classic,
    PureConfig.pureConfig,
    ScalaTest.scalatest % Test
  )
)

lazy val root = (project in file("."))
  .settings(
    name := "morecat2"
  )
  .aggregate(bootstrap, ui)

lazy val bootstrap = (project in file("bootstrap"))
  .settings(commonSettings)
  .settings(
    name := "bootstrap"
  )
  .dependsOn(ui)

lazy val ui = (project in file("ui"))
  .settings(commonSettings)
  .settings(
    name := "ui",
    libraryDependencies ++= Seq(
      Http4s.blazeServer,
      Http4s.circe,
      Http4s.dsl
    )
  )
