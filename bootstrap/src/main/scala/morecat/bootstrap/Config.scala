package morecat.bootstrap

import cats.effect.IO
import pureconfig.error.ConfigReaderException

case class Config(server: ServerConfig)

object Config {
  def load(): IO[Config] = {
    IO {
      pureconfig.loadConfig[Config]
    }.flatMap {
      case Left(e)       => IO.raiseError[Config](ConfigReaderException[Config](e))
      case Right(config) => IO.pure(config)
    }
  }
}

case class ServerConfig(host: String, port: Int)
