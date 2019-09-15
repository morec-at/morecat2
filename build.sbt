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

val commandInfra = (project in file("command/infra"))
  .settings(
    libraryDependencies ++= Seq(
      Ulid4s.ulid4s
    )
  )

val commandDomain = (project in file("command/domain"))
  .settings(
    libraryDependencies ++= Seq(
      Cats.core
    )
  )
  .dependsOn(commandInfra)

val commandUseCase = (project in file("command/usecase"))
  .dependsOn(commandDomain)
