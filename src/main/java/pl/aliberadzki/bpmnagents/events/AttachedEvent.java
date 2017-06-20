package pl.aliberadzki.bpmnagents.events;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public abstract class AttachedEvent implements EventListener, AttachedEventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;

    public AttachedEvent(BpmnAgent bpmnAgent, BoundaryEvent flowNode)
    {
        this.bpmnAgent = bpmnAgent;
        this.event = flowNode;
    }

    @Override
    public void afterFinish()
    {
        if(event.cancelActivity()) {
            bpmnAgent.reactToBoundaryInterrupt(event);
        }
    }

    public String getAttachedToId()
    {
        return event.getAttachedTo().getId();
    }
}