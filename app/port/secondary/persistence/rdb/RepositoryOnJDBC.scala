package port.secondary.persistence.rdb

import domain.support.IOContext
import port.secondary.persistence.rdb.support.IOContextOnJDBC
import scalikejdbc.DBSession

trait RepositoryOnJDBC {

  protected def withDBSession[A](ctx: IOContext)(f: DBSession => A): A = {
    ctx match {
      case IOContextOnJDBC(dbSession) => f(dbSession)
      case _ =>
        throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

}
