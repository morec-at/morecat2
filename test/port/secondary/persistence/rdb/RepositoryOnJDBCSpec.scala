package port.secondary.persistence.rdb

import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.{fixture, DiagrammedAssertions}
import scalikejdbc.scalatest.AsyncAutoRollback
import test.InjectSupport

trait RepositoryOnJDBCSpec
    extends fixture.AsyncFlatSpec
    with AsyncAutoRollback
    with DbSettings
    with DockerTestKit
    with DockerMoreCatDBService
    with InjectSupport
    with DiagrammedAssertions {

  override def fixture(implicit session: FixtureParam): Unit = {
    init
    fixtureWithInit
  }

  def fixtureWithInit(implicit session: FixtureParam): Unit

}
