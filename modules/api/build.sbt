name := "morecat-api"
organization := "morecat"
scalaVersion := "2.13.8"

enablePlugins(AkkaserverlessPlugin, JavaAppPackaging, DockerPlugin)
Compile / scalacOptions ++= Seq(
  "-target:11",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint",
  "-Werror"
)
(Compile / console / scalacOptions) ~= { _.filterNot(_ == "-Werror") }
Compile / javacOptions ++= Seq(
  // for Jackson
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-parameters"
)

Test / parallelExecution := false
Test / testOptions += Tests.Argument("-oDF")
Test / logBuffered := false

Compile / run := {
  // needed for the proxy to access the user function on all platforms
  sys.props += "akkaserverless.user-function-interface" -> "0.0.0.0"
  (Compile / run).evaluated
}
run / fork := false
Global / cancelable := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)
