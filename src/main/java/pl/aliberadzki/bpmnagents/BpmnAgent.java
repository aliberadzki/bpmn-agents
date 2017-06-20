package pl.aliberadzki.bpmnagents;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.camunda.bpm.model.bpmn.instance.*;
import pl.aliberadzki.bpmnagents.behaviours.*;
import pl.aliberadzki.bpmnagents.behaviours.Activity;
import pl.aliberadzki.bpmnagents.interpreter.BpmnInterpreter;
import pl.aliberadzki.bpmnagents.knowledge.Belief;
import pl.aliberadzki.bpmnagents.knowledge.Expression;
import pl.aliberadzki.bpmnagents.knowledge.Knowledge;
import pl.aliberadzki.bpmnagents.ontologies.booktrading.BookTradingOntology;

import java.util.*;

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
        this.registerService(participantId);
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

    public Collection<AID> findReceivers(SendTask sendTask) {
        return this.findServiceProviders(interpreter.getParticipantIdForSenderTask(sendTask));
    }

    public void registerService(String serviceType)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public Collection<AID> findServiceProviders(String serviceType)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        dfd.addServices(sd);
        Collection<AID> providers = new ArrayList<>();
        try {
            DFAgentDescription[] agentDescriptions = DFService.search(this, dfd);
            for(DFAgentDescription ad : agentDescriptions) {
                providers.add(ad.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        return providers;
    }

    public String getConversationId(Task task)
    {
        return interpreter
                .getMessageFlowForTask(task)
                .map(BaseElement::getId)
                .orElse("UNKNOWN");
    }

    public String generateMsgContentFromInputs(Map<String, Belief> inputs)
    {
        return inputs
                .entrySet()
                .stream()
                .map(Knowledge::stringifyBeliefEntry)
                .reduce(",", String::concat);
    }

    public void blockBehaviour(Activity activity, long period) {
        this.interpreter.blockBpmnBehaviour(activity, period);
    }
}
