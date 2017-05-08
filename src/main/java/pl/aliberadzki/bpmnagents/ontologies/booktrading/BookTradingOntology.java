package pl.aliberadzki.bpmnagents.ontologies.booktrading;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.*;

/**
 * Created by aliberadzki on 08.05.17.
 */
public class BookTradingOntology extends Ontology implements BookTradingVocabulary {
    // The name identifying this ontology
    public static final String ONTOLOGY_NAME = "Book-trading-ontology";

    // The singleton instance of this ontology
    private static Ontology theInstance = new BookTradingOntology();

    // Retrieve the singleton Book-trading ontology instance
    public static Ontology getInstance() {
        return theInstance;
    }

    // Private constructor
    private BookTradingOntology() {
        // The Book-trading ontology extends the basic ontology
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(new ConceptSchema(BOOK), Book.class);
            add(new PredicateSchema(COSTS), Costs.class);
            add(new AgentActionSchema(SELL), Sell.class);

            // Structure of the schema for the Book concept
            ConceptSchema cs = (ConceptSchema) getSchema(BOOK);
            cs.add(BOOK_TITLE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(BOOK_AUTHORS, (PrimitiveSchema) getSchema(BasicOntology.STRING), 0,
                    ObjectSchema.UNLIMITED);
            cs.add(BOOK_EDITOR, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);

            // Structure of the schema for the Costs predicate
            PredicateSchema ps = (PredicateSchema) getSchema(COSTS);
            ps.add(COSTS_ITEM, (ConceptSchema) cs);
            ps.add(COSTS_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            // Structure of the schema for the Sell agent action
            AgentActionSchema as = (AgentActionSchema) getSchema(SELL);
            as.add(SELL_ITEM, (ConceptSchema) getSchema(BOOK));
        }
        catch (OntologyException oe) {
            oe.printStackTrace();
        }
    }
}
