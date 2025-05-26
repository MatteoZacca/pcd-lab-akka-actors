package it.unibo.pcd.akka.basics.e01hello

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Terminated}

object Killable:
  def apply(): Behavior[String] =
    Behaviors.receiveMessage[String]:
      case "kill" => Behaviors.stopped
      case _ => Behaviors.same

object Root:
  def apply(): Behavior[String] =
    Behaviors.setup[String]: context =>
      val child = context.spawn(Killable(), "Killable") // creating child actor using ActorContext[T].spawn(Behavior[T], name)
      /* context.watch(child) registra l'attore corrente (Root) come osservatore del ciclo 
      di vita del figlio child. Se il figlio Killable termina (sia per un errore sia 
      volontariamente con Behaviors.stopped), l'attore padre riceverà un segnale di tipo
      Terminated */
      context.watch(child)
      // La variabile child contiene un riferimento all'attore figlio (ActorRef[String])
      // appena creato, che permette di inviargli messaggi
      child ! "kill"
      //context.stop(child)
      /* Questo blocco specifica come l'attore gestisce i segnali di sistema come Terminated
      (segnale inviato quando un attore osservato termina) o altri segnali legati al ciclo
      di vita o al contesto */
      Behaviors.receiveSignal:
        // case (_,Terminated(_)) =>
        case (context, Terminated(ref)) =>
          context.log.info("Child terminated")
          Behaviors.stopped

@main def killExample() =
  val system = ActorSystem[String](
    guardianBehavior = Root(),
    name = "Root"
  )
  system ! "kill"
  Thread.sleep(5000)
  system.terminate()
