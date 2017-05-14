package pl.aliberadzki.bpmnagents;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.interpreter.BpmnInterpreter;
import pl.aliberadzki.bpmnagents.knowledge.Expression;
import pl.aliberadzki.bpmnagents.knowledge.Knowledge;
import pl.aliberadzki.bpmnagents.ontologies.booktrading.BookTradingOntology;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class BpmnAgent extends Agent {

    private BpmnInterpreter interpreter;
    private Codec codec = new SLCodec();
    private Ontology ontology = BookTradingOntology.getInstance();
    private Knowledge knowledge = new Knowledge();

    @Override
    protected void setup()
    {
        this.initParameters();

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
    }

    private void initParameters()
    {
        Object[] args = getArguments();
        String bpdName = (String) args[0];
        String participantId = (String) args[1];
        List<Object> paramList = Arrays.asList(args).subList(2, args.length);
        this.interpreter = new BpmnInterpreter(this, bpdName, participantId, paramList);
    }

    public boolean evaluateExpression(Expression expression)
    {
        return knowledge.evaluateExpression(expression);
    }

    public void recognizeFact(String key, Object value)
    {
        knowledge.recognizeFact(key, value);
    }

    public void log(String content)
    {
        System.out.println('['+this.getAID().getLocalName() + "] " + content );
    }

    public void processStarted() {
        interpreter.removeProcessStarters();
    }

    public void addBoundaryEventsFor(Task task)
    {
        interpreter.scheduleBoundaryEventsFor(task);
    }

    public void clearBoundaryEventsFor(Task task)
    {
        interpreter.cancelBoundaryEventsFor(task);
    }

    public boolean anyIncomingRouteActive(FlowNode flowNode)
    {
        return interpreter.isAnyIncomingRouteActive(flowNode);
    }

    public boolean allIncomingRoutesActive(FlowNode flowNode)
    {
        return interpreter.areAllIncomingRoutesActive(flowNode);
    }

    public void activateFlows(Collection<SequenceFlow> sequenceFlows)
    {
        interpreter.activateFlows(sequenceFlows);
    }

    public void deactivateFlows(Collection<SequenceFlow> sequenceFlows)
    {
        interpreter.deactivateFlows(sequenceFlows);
    }

    public void reactToBoundaryInterrupt(BoundaryEvent event)
    {
        interpreter.cancelActivityWithAttachee(event);
    }

    public Object getProcessValue(String expressionString)
    {
        return knowledge.factValue(expressionString);
    }

    public Collection<AID> findReciever(String sendTaskId) {
        Collection<String> receiverIds = interpreter.getReceiversForSender(sendTaskId);
        return null;
    }
}
