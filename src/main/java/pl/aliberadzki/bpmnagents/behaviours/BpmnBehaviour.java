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
    private BpmnAgent bpmnAgent;
    private FlowNode flowNode;
    protected boolean done = false;

    public BpmnBehaviour(BpmnAgent bpmnAgent, FlowNode flowNode)
    {
        super(bpmnAgent);
        this.bpmnAgent = bpmnAgent;
        this.flowNode = flowNode;
    }

    public String getId()
    {
        return this.flowNode.getId();
    }

    @Override
    public void action()
    {
        if(canRun()) {
            this.beforeFinish();
            this.done = this.execute();
            if(done) {
                this.deactivate(flowNode.getIncoming());
                //TODO filter which should be marked as active?
                this.activate(getOutgoing());
                this.afterFinish();
            }
        }
        else blockBehaviour();
    }

    protected Collection<SequenceFlow> getOutgoing() {
        return flowNode.getOutgoing();
    }

    @Override
    public boolean done()
    {
        return this.done;
    }

    protected abstract boolean canRun();

    protected abstract boolean execute();

    protected void beforeFinish() {}

    protected void afterFinish() {}

    protected void blockBehaviour() {
        this.block();
    }

    protected boolean anyIncomingRouteActive()
    {
        return bpmnAgent.anyIncomingRouteActive(flowNode);
    }

    protected boolean allIncomingRoutesActive()
    {
        return bpmnAgent.allIncomingRoutesActive(flowNode);
    }

    private void activate(Collection<SequenceFlow> outgoingSequenceFlows)
    {
        bpmnAgent.activateFlows(outgoingSequenceFlows);
    }

    private void deactivate(Collection<SequenceFlow> sequenceFlows)
    {
        bpmnAgent.deactivateFlows(sequenceFlows);
    }
}
