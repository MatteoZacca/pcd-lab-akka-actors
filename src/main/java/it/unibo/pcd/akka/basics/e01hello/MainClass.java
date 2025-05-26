package it.unibo.pcd.akka.basics.e01hello;

import akka.actor.typed.ActorSystem;

public class MainClass {

    public static void main(String[] args) {

        log("Punto di ingresso del programma");
        // An actor system is a hierarchical group of actors which share common configuration
        ActorSystem<HelloActorJava.Greet> system = ActorSystem.create(HelloActorJava.create(), "hello-world");
        log("Ho appena passato la creazione dell'actor system");
        system.tell(new HelloActorJava.Greet("Akka Typed", system.ignoreRef()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system.terminate();
    }

    private static void log (String msg) {
        System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
    }
}
