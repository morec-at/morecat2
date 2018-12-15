import Dependencies._

inThisBuild(
  Seq(
    organization := "morecat",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
  )
)

lazy val commonSettings = Seq(
  scalacOptions ++= ScalacOptions.enabled,
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
  .aggregate(domain, bootstrap, ui, infrastructure)

lazy val domain = (project in file("domain"))
  .settings(commonSettings)
  .settings(
    name := "domain"
  )

lazy val usecase = (project in file("usecase"))
  .settings(commonSettings)
  .settings(
    name := "usecase"
  )
  .dependsOn(domain)

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
  .dependsOn(domain, usecase)

lazy val infrastructure = (project in file("infrastructure"))
  .settings(commonSettings)
  .settings(
    name := "infrastructure",
    libraryDependencies ++= Seq(
      Doobie.doobie
    )
  )
  .dependsOn(domain)

lazy val bootstrap = (project in file("bootstrap"))
  .settings(commonSettings)
  .settings(
    name := "bootstrap",
    libraryDependencies ++= Seq(
      Airframe.di
    )
  )
  .dependsOn(ui, infrastructure)
