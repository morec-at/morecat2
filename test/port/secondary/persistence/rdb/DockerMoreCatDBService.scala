package port.secondary.persistence.rdb

import java.sql.DriverManager

import com.spotify.docker.client.DefaultDockerClient
import com.typesafe.config.{Config, ConfigFactory}
import com.whisk.docker.impl.spotify.SpotifyDockerFactory
import com.whisk.docker.{
  DockerCommandExecutor,
  DockerContainer,
  DockerContainerState,
  DockerFactory,
  DockerKit,
  DockerReadyChecker
}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait DockerMoreCatDBService extends DockerKit {

  val config: Config = ConfigFactory.load()

  def MariaDBAdvertisedPort = 3306

  def MariaDBExposedPort = 33306

  val MySQLRootUser = "root"
  val MySQLDataBase = config.getString("db.default.name")
  val MySQLUser     = config.getString("db.default.username")
  val MySQLPassword = config.getString("db.default.password")

  val morecatdbContainer = DockerContainer(config.getString("docker.db.image"))
    .withPorts((MariaDBAdvertisedPort, Some(MariaDBExposedPort)))
    .withEnv(s"MYSQL_ROOT_PASSWORD=$MySQLRootUser",
             s"MYSQL_DATABASE=$MySQLDataBase",
             s"MYSQL_USER=$MySQLUser",
             s"MYSQL_PASSWORD=$MySQLPassword")
    .withReadyChecker(
      new MariaDBReadyChecker(config.getString("db.default.url"), MySQLUser, MySQLPassword)
        .looped(15, 1.second)
    )

  abstract override def dockerContainers: List[DockerContainer] = morecatdbContainer :: super.dockerContainers

  override implicit val dockerFactory: DockerFactory = new SpotifyDockerFactory(DefaultDockerClient.fromEnv().build())
}

private class MariaDBReadyChecker(url: String, user: String, password: String) extends DockerReadyChecker {

  override def apply(container: DockerContainerState)(implicit docker: DockerCommandExecutor,
                                                      ec: ExecutionContext): Future[Boolean] =
    container
      .getPorts()
      .map(
        ports =>
          Try {
            Option(DriverManager.getConnection(url, user, password))
              .map(_.close)
              .isDefined
          }.getOrElse(false)
      )

}
