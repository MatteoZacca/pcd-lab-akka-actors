package it.unibo.pcd.akka.basics.e01hello

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Terminated}
import ChildActor.*

object ChildActor:
  def apply(): Behavior[String] =
    Behaviors.receive: (context, msg) =>
      msg match
        case "Fine dei giochi" =>
          context.log.info(s"Messaggio object ChildActor: $msg")
          Behaviors.stopped
        case _ => 
          context.log.info(s"Messaggio object ChildActor: $msg")
          Behaviors.same

object RootActor:
  def apply(): Behavior[String] =
    Behaviors.setup: context =>
      val child = context.spawn(ChildActor(), "Actor-Child")
      context.watch(child)
      child ! "Vale"
      child ! "Yellow"
      child ! "46"
      child ! "Fine dei giochi"
      child ! "Non posso più inviare messaggi"
      
      Behaviors
        .receiveMessage[String]:
          msg =>
            context.log.info(s"Messaggio object RootActor: $msg")
            Behaviors.same
        .receiveSignal:
          case (context, Terminated(ref)) =>
            context.log.info("ChildActor terminated")
            Behaviors.stopped

@main def childActorExample: Unit =
  val actorSystem = ActorSystem[String](
    guardianBehavior = RootActor(),
    name = "Actor-Root"
  )
  actorSystem ! "Ammutinati"
  actorSystem ! "Appesi!"
  
  
  


