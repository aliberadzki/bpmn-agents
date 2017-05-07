package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class MsgBoundaryEventBehaviour extends BoundaryEventBehaviour {
    private final BoundaryEvent event;
    private ACLMessage msg = null;
    private String msgId;

    public MsgBoundaryEventBehaviour(BpmnAgent agent, BoundaryEvent event) {
        super(agent, event);
        this.event = event;
        this.msgId = ((MessageEventDefinition)event.getEventDefinitions().iterator().next())
                .getMessage().getId();
    }

    @Override
    protected boolean canRun() {
        this.msg = myAgent.receive(MessageTemplate.MatchConversationId(msgId));
        return this.msg != null;
    }

    @Override
    protected boolean execute() {
        System.out.println("Boundary event ran! (msg: "
                + this.msg.getContent()
                + ") cancelActivity: "
                + event.cancelActivity());
        return true;
    }
}
