import Dependencies.*

ThisBuild / organization := "morecat"
ThisBuild / version := "2.0.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.0"
ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-indent",
  "-print-lines",
  "-explain",
  "-Werror",
  "-Wunused:all",
  "-Wvalue-discard",
  "-Wnonunit-statement"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "api",
    libraryDependencies ++= Seq(
      zioHttp
    )
  )
