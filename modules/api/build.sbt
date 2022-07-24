ThisBuild / organization := "morecat"

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / Compile / scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint",
  "-Werror"
)
(Compile / console / scalacOptions) ~= { _.filterNot(_ == "-Werror") }

ThisBuild / libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.12" % Test
)

lazy val bootstrap = (project in file("."))
  .aggregate(transport)
  .dependsOn(transport)
  .settings(
    libraryDependencies ++= Seq()
  )

lazy val transport = (project in file("transport"))
  .enablePlugins(AkkaGrpcPlugin)
