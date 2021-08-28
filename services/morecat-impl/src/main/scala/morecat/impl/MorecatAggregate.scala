package morecat.impl

import akka.actor.typed.{ActorRef, Behavior}
import akka.cluster.sharding.typed.scaladsl._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, ReplyEffect}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AkkaTaggerAdapter}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json._

import java.time.LocalDateTime
import scala.collection.immutable.Seq

/** This provides an event sourced behavior. It has a state, [[MorecatState]], which stores what the greeting should be
  * (eg, "Hello").
  *
  * Event sourced entities are interacted with by sending them commands. This aggregate supports two commands, a
  * [[UseGreetingMessage]] command, which is used to change the greeting, and a [[Hello]] command, which is a read only
  * command which returns a greeting to the name specified by the command.
  *
  * Commands get translated to events, and it's the events that get persisted. Each event will have an event handler
  * registered for it, and an event handler simply applies an event to the current state. This will be done when the
  * event is first created, and it will also be done when the aggregate is loaded from the database - each event will be
  * replayed to recreate the state of the aggregate.
  *
  * This aggregate defines one event, the [[GreetingMessageChanged]] event, which is emitted when a
  * [[UseGreetingMessage]] command is received.
  */
object MorecatBehavior {

  /** Given a sharding [[EntityContext]] this function produces an Akka [[Behavior]] for the aggregate.
    */
  def create(entityContext: EntityContext[MorecatCommand]): Behavior[MorecatCommand] = {
    val persistenceId: PersistenceId = PersistenceId(entityContext.entityTypeKey.name, entityContext.entityId)

    create(persistenceId)
      .withTagger(
        // Using Akka Persistence Typed in Lagom requires tagging your events
        // in Lagom-compatible way so Lagom ReadSideProcessors and TopicProducers
        // can locate and follow the event streams.
        AkkaTaggerAdapter.fromLagom(entityContext, MorecatEvent.Tag)
      )

  }
  /*
   * This method is extracted to write unit tests that are completely independendant to Akka Cluster.
   */
  private[impl] def create(persistenceId: PersistenceId) = EventSourcedBehavior
    .withEnforcedReplies[MorecatCommand, MorecatEvent, MorecatState](
      persistenceId = persistenceId,
      emptyState = MorecatState.initial,
      commandHandler = (cart, cmd) => cart.applyCommand(cmd),
      eventHandler = (cart, evt) => cart.applyEvent(evt)
    )
}

/** The current state of the Aggregate.
  */
case class MorecatState(message: String, timestamp: String) {
  def applyCommand(cmd: MorecatCommand): ReplyEffect[MorecatEvent, MorecatState] =
    cmd match {
      case x: Hello              => onHello(x)
      case x: UseGreetingMessage => onGreetingMessageUpgrade(x)
    }

  def applyEvent(evt: MorecatEvent): MorecatState =
    evt match {
      case GreetingMessageChanged(msg) => updateMessage(msg)
    }
  private def onHello(cmd: Hello): ReplyEffect[MorecatEvent, MorecatState] =
    Effect.reply(cmd.replyTo)(Greeting(s"$message, ${cmd.name}!"))

  private def onGreetingMessageUpgrade(
      cmd: UseGreetingMessage
  ): ReplyEffect[MorecatEvent, MorecatState] =
    Effect
      .persist(GreetingMessageChanged(cmd.message))
      .thenReply(cmd.replyTo) { _ =>
        Accepted
      }

  private def updateMessage(newMessage: String) =
    copy(newMessage, LocalDateTime.now().toString)
}

object MorecatState {

  /** The initial state. This is used if there is no snapshotted state to be found.
    */
  def initial: MorecatState = MorecatState("Hello", LocalDateTime.now.toString)

  /** The [[EventSourcedBehavior]] instances (aka Aggregates) run on sharded actors inside the Akka Cluster. When
    * sharding actors and distributing them across the cluster, each aggregate is namespaced under a typekey that
    * specifies a name and also the type of the commands that sharded actor can receive.
    */
  val typeKey: EntityTypeKey[MorecatCommand] = EntityTypeKey[MorecatCommand]("MorecatAggregate")

  /** Format for the hello state.
    *
    * Persisted entities get snapshotted every configured number of events. This means the state gets stored to the
    * database, so that when the aggregate gets loaded, you don't need to replay all the events, just the ones since the
    * snapshot. Hence, a JSON format needs to be declared so that it can be serialized and deserialized when storing to
    * and from the database.
    */
  implicit val format: Format[MorecatState] = Json.format
}

/** This interface defines all the events that the MorecatAggregate supports.
  */
sealed trait MorecatEvent extends AggregateEvent[MorecatEvent] {
  def aggregateTag: AggregateEventTag[MorecatEvent] = MorecatEvent.Tag
}

object MorecatEvent {
  val Tag: AggregateEventTag[MorecatEvent] = AggregateEventTag[MorecatEvent]()
}

/** An event that represents a change in greeting message.
  */
case class GreetingMessageChanged(message: String) extends MorecatEvent

object GreetingMessageChanged {

  /** Format for the greeting message changed event.
    *
    * Events get stored and loaded from the database, hence a JSON format needs to be declared so that they can be
    * serialized and deserialized.
    */
  implicit val format: Format[GreetingMessageChanged] = Json.format
}

/** This is a marker trait for commands. We will serialize them using Akka's Jackson support that is able to deal with
  * the replyTo field. (see application.conf)
  */
trait MorecatCommandSerializable

/** This interface defines all the commands that the MorecatAggregate supports.
  */
sealed trait MorecatCommand extends MorecatCommandSerializable

/** A command to switch the greeting message.
  *
  * It has a reply type of [[Confirmation]], which is sent back to the caller when all the events emitted by this
  * command are successfully persisted.
  */
case class UseGreetingMessage(message: String, replyTo: ActorRef[Confirmation]) extends MorecatCommand

/** A command to say hello to someone using the current greeting message.
  *
  * The reply type is String, and will contain the message to say to that person.
  */
case class Hello(name: String, replyTo: ActorRef[Greeting]) extends MorecatCommand

final case class Greeting(message: String)

object Greeting {
  implicit val format: Format[Greeting] = Json.format
}

sealed trait Confirmation

case object Confirmation {
  implicit val format: Format[Confirmation] = new Format[Confirmation] {
    override def reads(json: JsValue): JsResult[Confirmation] = {
      if ((json \ "reason").isDefined)
        Json.fromJson[Rejected](json)
      else
        Json.fromJson[Accepted](json)
    }

    override def writes(o: Confirmation): JsValue = {
      o match {
        case acc: Accepted => Json.toJson(acc)
        case rej: Rejected => Json.toJson(rej)
      }
    }
  }
}

sealed trait Accepted extends Confirmation

case object Accepted extends Accepted {
  implicit val format: Format[Accepted] =
    Format(Reads(_ => JsSuccess(Accepted)), Writes(_ => Json.obj()))
}

case class Rejected(reason: String) extends Confirmation

object Rejected {
  implicit val format: Format[Rejected] = Json.format
}

/** Akka serialization, used by both persistence and remoting, needs to have serializers registered for every type
  * serialized or deserialized. While it's possible to use any serializer you want for Akka messages, out of the box
  * Lagom provides support for JSON, via this registry abstraction.
  *
  * The serializers are registered here, and then provided to Lagom in the application loader.
  */
object MorecatSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    // state and events can use play-json, but commands should use jackson because of ActorRef[T] (see application.conf)
    JsonSerializer[GreetingMessageChanged],
    JsonSerializer[MorecatState],
    // the replies use play-json as well
    JsonSerializer[Greeting],
    JsonSerializer[Confirmation],
    JsonSerializer[Accepted],
    JsonSerializer[Rejected]
  )
}
