package port.secondary.persistence.rdb

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import scalikejdbc.DBSession
import scalikejdbc.config.DBs

trait DbSettings {
  DBs.setupAll()

  def init(implicit session: DBSession): Unit = {
    val config = ConfigFactory.load()
    val flyway = new Flyway
    flyway.setDataSource(config.getString("db.default.url"),
                         config.getString("db.default.username"),
                         config.getString("db.default.password"))
    flyway.migrate()
  }
}
