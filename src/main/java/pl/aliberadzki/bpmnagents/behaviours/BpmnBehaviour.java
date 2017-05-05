package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

/**
 * Created by aliberadzki on 05.05.17.
 */
public abstract class BpmnBehaviour extends Behaviour {
    private FlowNode flowNode;

    protected boolean done = false;

    public BpmnBehaviour(BpmnAgent agent, FlowNode flowNode)
    {
        super(agent);
        this.flowNode = flowNode;
    }

    protected abstract boolean canRun();

    protected abstract boolean execute();

    protected abstract void beforeFinish();

    protected abstract void afterFinish();

    @Override
    public void action() {
        if(canRun()) {
            this.beforeFinish();
            this.done = this.execute();
            this.deactivate(flowNode.getIncoming());
            this.markAsActive(flowNode.getOutgoing());
            this.afterFinish();
        }
        block();
    }

    @Override
    public boolean done() {
        return this.done;
    }

    private void deactivate(Collection<SequenceFlow> sequenceFlows)
    {
        sequenceFlows.forEach(sequenceFlow -> ((BpmnAgent)myAgent).finish(sequenceFlow));
    }

    protected boolean anyIncomingRouteActive() {
        return flowNode.getIncoming().stream()
                .anyMatch(sequenceFlow -> ((BpmnAgent)myAgent).isActive(sequenceFlow));
    }

    protected boolean allIncomingRouteActive() {
        return flowNode.getIncoming().stream()
                .allMatch(sequenceFlow -> ((BpmnAgent)myAgent).isActive(sequenceFlow));
    }

    private void markAsActive(Collection<SequenceFlow> outgoingSequenceFlows)
    {
        //TODO filter which should be marked as active?
        ((BpmnAgent)myAgent).markAsActive(outgoingSequenceFlows);
        outgoingSequenceFlows
                .forEach(sequenceFlow -> ((BpmnAgent)myAgent).scheduleActivity(sequenceFlow.getTarget()));
    }


}
