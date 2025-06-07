package morecat.api

import zio.*
import zio.http.*

object Main extends ZIOAppDefault {

  val routes = Routes(
    Method.GET / Root -> handler(Response.text("Greeting at your service")),
    Method.GET / "greet" -> handler { (req: Request) =>
      val name = req.queryOrElse[String]("name", "World")
      Response.text(s"Hello $name")
    }
  )

  override def run = {
    Server.serve(routes).provide(Server.default)
  }
}
