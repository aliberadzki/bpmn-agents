package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

/**
 * Created by aliberadzki on 05.05.17.
 */
public class BpmnBehaviour extends Behaviour {
    private BpmnAgent bpmnAgent;
    private FlowNode flowNode;
    private Activity activity;
    protected boolean done = false;

    public BpmnBehaviour(BpmnAgent bpmnAgent, FlowNode flowNode, Activity activity)
    {
        super(bpmnAgent);
        this.bpmnAgent = bpmnAgent;
        this.flowNode = flowNode;
        this.activity = activity;
    }

    public String getId()
    {
        return this.flowNode.getId();
    }

    public Activity getActivity()
    {
        return activity;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }

    @Override
    public void action()
    {
        if(canRun()) {
            this.beforeFinish();
            this.done = this.execute();
            if(done) {
                this.deactivate(flowNode.getIncoming());
                this.activate(getOutgoing());
                this.afterFinish();
            }
        }
        else blockBehaviour();
    }

    protected Collection<SequenceFlow> getOutgoing() {
        return flowNode.getOutgoing();
    }

    protected boolean canRun()
    {
        return activity.isReady();
    }

    protected boolean execute()
    {
        return activity.execute();
    }

    protected void beforeFinish() {}

    protected void afterFinish() {
        activity.afterFinish();
    }

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
