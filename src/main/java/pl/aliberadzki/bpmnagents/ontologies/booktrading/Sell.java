package pl.aliberadzki.bpmnagents.ontologies.booktrading;

import jade.content.AgentAction;

/**
 * Created by aliberadzki on 08.05.17.
 */
public class Sell implements AgentAction {
    private Book item;

    public Book getItem() {
        return item;
    }

    public void setItem(Book item) {
        this.item = item;
    }

}
