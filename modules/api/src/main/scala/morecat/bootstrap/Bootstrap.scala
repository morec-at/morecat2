package morecat.bootstrap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import morecat.transport.grpc.{BlogServiceHandler, BlogServiceImpl}

import scala.concurrent.{ExecutionContext, Future}

class Bootstrap(system: ActorSystem) {
  def run(): Future[Http.ServerBinding] = {
    implicit val sys: ActorSystem = system
    implicit val ec: ExecutionContext = sys.dispatcher

    val service: HttpRequest => Future[HttpResponse] =
      BlogServiceHandler.withServerReflection(new BlogServiceImpl())
    val bound = Http().newServerAt("0.0.0.0", 8081).bind(service)
    bound.foreach { b => println(s"Morecat Server bound to: ${b.localAddress}") }
    bound
  }
}

object Bootstrap extends App {
  new Bootstrap(ActorSystem("Morecat")).run()
}
