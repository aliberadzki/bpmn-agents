package pl.aliberadzki.bpmnagents.examples.booktrading;

import jade.core.Agent;

/**
 * Created by aliberadzki on 03.05.17.
 */
public class BookBuyerAgent extends Agent {
    @Override
    protected void setup() {
        //Printout a welcome message
        System.out.println("Hello! Buyer agent " + getAID().getName() + " is ready");
        System.out.println("i have an argument of " + getArguments()[0]);
    }
}
