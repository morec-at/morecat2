name := """morecat2"""
organization := "at.morec"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

val scalikeJdbcVersion     = "3.2.0"
val scalikeJdbcPlayVersion = "2.6.0-scalikejdbc-3.2"
val dockerTestKitVersion   = "0.9.6"

libraryDependencies ++= Seq(
  guice,
  "org.scalikejdbc"        %% "scalikejdbc"                  % scalikeJdbcVersion,
  "org.scalikejdbc"        %% "scalikejdbc-config"           % scalikeJdbcVersion,
  "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % scalikeJdbcPlayVersion,
  "org.mariadb.jdbc"       % "mariadb-java-client"           % "2.2.1",
  "org.flywaydb"           %% "flyway-play"                  % "4.0.0",
  "net.codingwell"         %% "scala-guice"                  % "4.1.1",
  "com.iheart"             %% "ficus"                        % "1.4.3",
  "org.scalatestplus.play" %% "scalatestplus-play"           % "3.1.2" % Test,
  "org.mockito"            % "mockito-core"                  % "2.13.0" % Test,
  "org.scalikejdbc"        %% "scalikejdbc-test"             % scalikeJdbcVersion % Test,
  "com.whisk"              %% "docker-testkit-scalatest"     % dockerTestKitVersion % Test,
  "com.whisk"              %% "docker-testkit-impl-spotify"  % dockerTestKitVersion % Test
)
javaOptions in Test += "-Dconfig.file=conf/application-test.conf"
