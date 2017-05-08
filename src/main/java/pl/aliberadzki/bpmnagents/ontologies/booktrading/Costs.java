package pl.aliberadzki.bpmnagents.ontologies.booktrading;

import jade.content.Predicate;

/**
 * Created by aliberadzki on 08.05.17.
 */
public class Costs implements Predicate {
    private Book item;
    private int price;

    public Book getItem() {
        return item;
    }

    public void setItem(Book item) {
        this.item = item;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
