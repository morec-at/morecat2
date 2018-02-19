import com.google.inject.AbstractModule
import domain.lifecycle.blog.ArticleRepository
import domain.support.IOContext
import net.codingwell.scalaguice.ScalaModule
import port.secondary.persistence.rdb.blog.ArticleRepositoryOnJDBC
import port.secondary.persistence.rdb.support.IOContextOnJDBC
import scalikejdbc.AutoSession

class AppModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[IOContext].toInstance(IOContextOnJDBC(AutoSession))

    bind[ArticleRepository].to[ArticleRepositoryOnJDBC]
  }

}
