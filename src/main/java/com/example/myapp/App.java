package com.example.myapp;
/**
 * mvn package
 * mvn exec:java -Dexec.mainClass="myapp.src.main.java.com.example.myapp.App"
 */
public class App {
    public static void main(String[] args) {

        ConsumerHandler c = new ConsumerHandler();
        try {
            c.consumerLoop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(" - - - - - Quit unexpectedly! - - - - - ");
            System.exit(0);
        }
    }
}
