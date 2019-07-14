import sbt._

object Dependencies {

  object Versions {
    val cats      = "1.6.1"
    val ulid4s    = "0.4.0"
    val scalaTest = "3.0.8"
  }

  object Cats {
    val core = "org.typelevel" %% "cats-core" % Versions.cats
  }

  object Ulid4s {
    val ulid4s = "net.petitviolet" %% "ulid4s" % Versions.ulid4s
  }

  object ScalaTest {
    val scalatest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  }

}
