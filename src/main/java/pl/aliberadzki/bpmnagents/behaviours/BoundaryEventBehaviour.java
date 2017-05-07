package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public abstract class BoundaryEventBehaviour extends BpmnBehaviour implements EventListener {
    private final BoundaryEvent event;

    public BoundaryEventBehaviour(BpmnAgent agent, BoundaryEvent flowNode) {
        super(agent, flowNode);
        this.event = flowNode;
    }

    @Override
    protected void afterFinish() {
        if(event.cancelActivity()) {
            ((BpmnAgent)myAgent).cancelBehaviour(event.getAttachedTo().getId());
        }
    }

    public String getAttachedToId()
    {
        return event.getAttachedTo().getId();
    }
}
