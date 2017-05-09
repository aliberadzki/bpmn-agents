package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public abstract class BoundaryEventBehaviour extends BpmnBehaviour implements EventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;

    public BoundaryEventBehaviour(BpmnAgent bpmnAgent, BoundaryEvent flowNode) {
        super(bpmnAgent, flowNode);
        this.bpmnAgent = bpmnAgent;
        this.event = flowNode;
    }

    @Override
    protected void afterFinish() {
        if(event.cancelActivity()) {
            bpmnAgent.reactToBoundaryInterrupt(event);
        }
    }

    public String getAttachedToId()
    {
        return event.getAttachedTo().getId();
    }
}