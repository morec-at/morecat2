package morecat.impl

import akka.cluster.sharding.typed.scaladsl.Entity
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceLocator}
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import morecat.api.MorecatService
import play.api.libs.ws.ahc.AhcWSComponents

class MorecatLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new MorecatApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MorecatApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[MorecatService])
}

abstract class MorecatApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[MorecatService](wire[MorecatServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = MorecatSerializerRegistry

  // Initialize the sharding of the Aggregate. The following starts the aggregate Behavior under
  // a given sharding entity typeKey.
  clusterSharding.init(
    Entity(MorecatState.typeKey)(entityContext => MorecatBehavior.create(entityContext))
  )

}
