package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class BoundaryEventBehaviour extends BpmnBehaviour implements EventListener {
    private final BoundaryEvent event;
    private ACLMessage msg = null;

    public BoundaryEventBehaviour(BpmnAgent agent, BoundaryEvent flowNode) {
        super(agent, flowNode);
        this.event = flowNode;
    }

    @Override
    protected boolean canRun() {
        this.msg = myAgent.receive();
        return this.msg != null;
    }

    @Override
    protected boolean execute() {
        System.out.println("Boundary event ran! (msg: "
                + this.msg.getContent()
                + ") cancelActivity: "
                + event.cancelActivity());
        if(event.cancelActivity()) {
            ((BpmnAgent)myAgent).cancelBehaviour(event.getAttachedTo().getId());
        }
        return true;
    }
}
