package it.unibo.pcd.akka.basics.e01hello;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.ActorRef;

public class HelloActorJava {
    // Messages that HelloActor can receive
    public static final class Greet {
        public final String whom;
        public final ActorRef<Greeted> replyTo;

        public Greet(String whom, ActorRef<Greeted> replyTo) {
            this.whom = whom;
            this.replyTo = replyTo;
            log("Sono entrato nel costruttore di Greet");
        }
    }

    public static final class Greeted {
        public final String whom;
        public final ActorRef<Greet> from;

        public Greeted(String whom, ActorRef<Greet> from) {
            this.whom = whom;
            this.from = from;
            log("Sono entrato nel costruttore di Greeted");
        }
    }

    // Actor behavior definition
    public static Behavior<Greet> create() {
        log("Sono appena entrato nel metodo create");
        return Behaviors.receive(Greet.class) // specifica che questo attore è progettato
                // per ricevere messaggi del tipo Greet
                .onMessage(Greet.class, HelloActorJava::onGreet)
                .build();
    }

    private static Behavior<Greet> onGreet(Greet message) {
        log("Sono appena entrato nel metodo onGreet");
        return Behaviors.setup(context -> {
            log("Hello " + message.whom + "!");
            message.replyTo.tell(new Greeted(message.whom, context.getSelf()));
            return Behaviors.same();
        });
    }

    private static void log (String msg) {
        System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
    }
}