ThisBuild / organization := "morecat"
ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "2.13.6"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.4.0" % Provided
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9" % Test

lazy val `morecat` = (project in file("."))
  .aggregate(`morecat-api`, `morecat-impl`)

lazy val `morecat-api` = (project in file("morecat-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `morecat-impl` = (project in file("morecat-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`morecat-api`)
