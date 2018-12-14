package morecat.bootstrap

import cats.effect.IO
import morecat.infrastructure.rdb.RdbConfig
import morecat.ui.ServerConfig
import pureconfig.error.ConfigReaderException

case class Config(server: ServerConfig, rdb: RdbConfig)

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
