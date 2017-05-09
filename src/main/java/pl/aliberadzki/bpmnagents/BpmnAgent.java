package pl.aliberadzki.bpmnagents;

import jade.core.Agent;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.interpreter.BpmnInterpreter;

import java.util.Collection;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class BpmnAgent extends Agent {
    private String bpdName;
    private String participantId;

    private BpmnInterpreter interpreter;

    @Override
    protected void setup()
    {
        this.bpdName = (String) getArguments()[0];
        this.participantId = (String) getArguments()[1];
        this.interpreter = new BpmnInterpreter(this, bpdName, participantId);
        //TODO init intermediate events WHEN process started
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
}
