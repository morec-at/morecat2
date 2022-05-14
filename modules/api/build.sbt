ThisBuild / organization := "morecat"

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / Compile / scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Werror"
)
(Compile / console / scalacOptions) ~= { _.filterNot(_ == "-Werror") }

ThisBuild / libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.12" % Test
)

lazy val root = (project in file(".")).aggregate(domain)

val AkkaVersion = "2.6.19"
lazy val domain = (project in file("domain")).settings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test
  )
)
