package it.unibo.pcd.akka.basics.e01hello

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

// "Actor" module definition, che contiene l'implementazione del nostro attore "HelloActor"
object HelloActor:
  // "API", i.e. message that actors should received / send
  // Le istanze di una case class sono immutabili per default
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])
  final case class Greeted(whom: String, from: ActorRef[Greet])
  // Behaviour factory, i.e how the actor react to messages
  // Behavior[Greet]: specifica il tipo restituito dal metodo. Il Behavior riguarda messaggi
  // di tipo Greet
  // Behaviors.receive: factory method fornito da Akka, restituisce un Behavior che specifica
  // come un attore risponde ai messaggi ricevuti, accetta una funzione anonima con parametri
  // (context, message) come parametro
  def apply(): Behavior[Greet] = Behaviors.receive: (context, message) =>
    // comportamento dell'attore
    context.log.info("Hello {}!", message.whom)
    message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same // indica che l'attore non cambia comportamento dopo aver processato
    // il messaggio

object HelloWorldAkkaTyped extends App:
  val system: ActorSystem[HelloActor.Greet] = ActorSystem(
    HelloActor(), 
    name = "hello-world"
  )
  system ! HelloActor.Greet("Akka Typed", system.ignoreRef)
  Thread.sleep(5000)
  system.terminate()
// Un sistema akka rimane attivo fino a quando almeno un attore è in grado di ricevere messaggi
