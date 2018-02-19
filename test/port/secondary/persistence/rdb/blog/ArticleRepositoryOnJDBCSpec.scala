package port.secondary.persistence.rdb.blog

import domain.support.IOContext
import port.secondary.persistence.rdb.RepositoryOnJDBCSpec
import port.secondary.persistence.rdb.support.{ExecutionContextOnJdbc, IOContextOnJDBC}
import scalikejdbc._

class ArticleRepositoryOnJDBCSpec extends RepositoryOnJDBCSpec {

  val articleFixtureSize = 3

  implicit val ec: ExecutionContextOnJdbc = inject[ExecutionContextOnJdbc]

  val sut = new ArticleRepositoryOnJDBC

  "resolveAll" should "return all articles" in { implicit s =>
    implicit val ctx: IOContext = IOContextOnJDBC(s)
    sut.resolveAll().map { allArticles =>
      assert(allArticles.size === articleFixtureSize)
    }
  }

  override def fixtureWithInit(implicit session: FixtureParam): Unit = {
    for (i <- 1 to articleFixtureSize) {
      sql"""
           |INSERT INTO articles (article_id, title, content, created_date_time, last_updated_date_time)
           |VALUES ($i, $i, $i, now(), now())
         """.stripMargin.update.apply
    }
  }

}
