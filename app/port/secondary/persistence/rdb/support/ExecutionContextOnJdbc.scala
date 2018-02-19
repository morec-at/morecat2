package port.secondary.persistence.rdb.support

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

@Singleton
class ExecutionContextOnJdbc @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "jdbc.dispatcher")
