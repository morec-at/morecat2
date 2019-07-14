import Dependencies._

inThisBuild(
  Seq(
    organization := "morecat",
    name := "morecat-api",
    version := "2.0.0-SNAPSHOT",
    scalaVersion := "2.12.8",
    scalacOptions ++= ScalacOptions.enabled,
    scalacOptions in (Compile, console) --= ScalacOptions.disabledInRepl,
    libraryDependencies ++= Seq(
      ScalaTest.scalatest % Test
    )
  )
)

val infra = (project in file("core/infra"))
  .settings(
    libraryDependencies ++= Seq(
      Ulid4s.ulid4s
    )
  )

val domain = (project in file("core/domain"))
  .settings(
    libraryDependencies ++= Seq(
      Cats.core
    )
  )
  .dependsOn(infra)
