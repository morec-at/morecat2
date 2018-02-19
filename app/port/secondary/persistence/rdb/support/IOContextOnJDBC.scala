package port.secondary.persistence.rdb.support

import domain.support.IOContext
import scalikejdbc.DBSession

case class IOContextOnJDBC(session: DBSession) extends IOContext
