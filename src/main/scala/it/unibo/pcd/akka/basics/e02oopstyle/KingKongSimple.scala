package it.unibo.pcd.akka.basics.e02oopstyle

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Terminated}

enum KingKong:
  case King
  case Kong
  
import KingKong.*  

class KingKonger(context: ActorContext[KingKong], var bounces: Int = 0) extends AbstractBehavior(context):
  context.log.info(s"Hello :), my path is: ${context.self.path}")

  override def onMessage(msg: KingKong): Behavior[KingKong] =
    if (bounces < 10) {
      msg match
        case King =>
          bounces += 1
          context.log.info("King")
          println("[" + Thread.currentThread().getName + s"] is printing: ---> ${context.self} ---> bounces: $bounces")
          context.self ! Kong
        case Kong =>
          bounces += 1
          context.log.info("Kong")
          println("[" + Thread.currentThread().getName + s"] is printing: ---> ${context.self} ---> bounces: $bounces")
          context.self ! King
      
      this
    } else {
      context.log.info("King Kong is tired, Hasta Luego hermano")
      Behaviors.stopped
    }


/* Abbiamo un singolo attore (KingKonger) che si automessaggia ---> KingKonger usa un enum 
KingKong con due soli casi che non trasportano referenze, e invia a sè stesso
alternativamente King/Kong.
Per l'invio di messaggi usa sempre context.self, perciò l'unica referenza è sé stesso. 
è quindi presente una sola mailbox che riceve e processa i messaggi in coda, uno alla volta. */
object KingKongMainSimple extends App:
  val system = ActorSystem[KingKong](
    Behaviors.setup(new KingKonger(_)),
    name = "king-kong"
  )
  system ! King
  system.terminate()
