import sbt._

object Dependencies {

  object Versions {
    val cats      = "1.6.1"
    val scalaTest = "3.0.8"
  }

  object Cats {
    val core = "org.typelevel" %% "cats-core" % Versions.cats,
  }

  object ScalaTest {
    val scalatest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  }

}
