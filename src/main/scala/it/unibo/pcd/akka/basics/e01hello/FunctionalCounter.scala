package it.unibo.pcd.akka.basics.e01hello

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior}
import FunctionalCounter.*

// "Actor" module definition
object FunctionalCounter:
  enum Command: // APIs i.e. message that actors should received / send
    case Tick
  /* Con export Command.* stiamo dicendo: tutti i membri pubblici di Command (in questo
  codice i ase dell'enum come Trick) sono riesportati da FunctionalCounter.
  Senza export Command.* per usare il case Tick avrei dovuto scrivere semore: 
  FunctionalCounter.Command.Tick */  
  export Command.*
  def apply(from: Int, to: Int): Behavior[Command] =
    Behaviors.receive: (context, msg) =>
      msg match
        case Tick if from != to =>
          context.log.info(s"Count: $from")
          FunctionalCounter(from - from.compareTo(to), to) // FunctionalCounter(from + 1, to)
        case _ =>
          context.log.info(s"I am done! $from")
          Behaviors.stopped // una volta che il guardian actor restituisce
          // Behaviors.stopped, Akka chiude quell'attore e non processa più altri
          // messaggi

// @main def functionalApi: Unit =
object Main extends App:
  val system = ActorSystem[Command](
    /* Comportamento del guardian actor, specificato tramite un Behavior[Command].
    Definisce come il guardian actor reagisce ai messaggi inviati all'ActorSystem */
    guardianBehavior = FunctionalCounter(0, 2), 
    // name: stringa che specifica il nome dell'ActorSystem
    name = "functional-counter" 
  )
  /* Vengono messi in coda tre messaggi Tick nella mailbox del guardian actor.
  L'invio è asincrono: il for termina subito, i messaggi restano in attesa di essere
  elaborati dall'actor */
  for (i <- 0 to 2) system ! Tick



