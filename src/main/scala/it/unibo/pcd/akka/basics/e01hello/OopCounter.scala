package it.unibo.pcd.akka.basics.e01hello

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior}
import OopCounter.*

// "Actor" module definition
object OopCounter:
  enum Command: 
    case Tick
  export Command.*
  
  def apply(from: Int, to: Int): Behavior[Command] =
    Behaviors.setup(new OopCounter(_, from, to))
    
class OopCounter(context: ActorContext[OopCounter.Command], var from: Int, val to: Int) 
    extends AbstractBehavior[OopCounter.Command](context):
  override def onMessage(msg: OopCounter.Command): Behavior[OopCounter.Command] = msg match
    case Tick if from != to =>
      context.log.info(s"Count: $from")
      from -= from.compareTo(to)
      this
      // Behaviors.same come alternativa a this

    case _ =>
      context.log.info(s"I am done! $from")
      Behaviors.stopped


@main def OOPApi: Unit =
  val system = ActorSystem[OopCounter.Command](OopCounter(0, 2), "oop-counter")
  for(i <- 0 to 2) system ! Tick
  
 
