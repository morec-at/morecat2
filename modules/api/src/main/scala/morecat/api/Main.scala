package morecat.api

import com.akkaserverless.scalasdk.AkkaServerless
import morecat.api.blog.BlogHandler
import org.slf4j.LoggerFactory

object Main {

  private val log = LoggerFactory.getLogger("morecat.api.Main")

  def createAkkaServerless(): AkkaServerless = {
    AkkaServerlessFactory.withComponents(new BlogHandler(_))
  }

  def main(args: Array[String]): Unit = {
    log.info("starting morecat api")
    createAkkaServerless().start()
  }
}
