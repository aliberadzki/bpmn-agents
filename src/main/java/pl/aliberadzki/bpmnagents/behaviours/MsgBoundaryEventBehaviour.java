package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.Message;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class MsgBoundaryEventBehaviour implements AttachedEventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;
    private ACLMessage msg = null;
    private String msgId;

    public MsgBoundaryEventBehaviour(BpmnAgent bpmnAgent, BoundaryEvent event, Message msg)
    {
        this.bpmnAgent = bpmnAgent;
        this.event = event;
        this.msgId = msg.getId();
    }

    @Override
    public boolean isReady()
    {
        this.msg = bpmnAgent.receive(MessageTemplate.MatchConversationId(msgId));
        return this.msg != null;
    }

    @Override
    public boolean execute() {
        bpmnAgent.log("Boundary event ran! (msg: "
                + this.msg.getContent()
                + ") cancelActivity: "
                + event.cancelActivity());
        return true;
    }

    @Override
    public void afterFinish() {

    }

    @Override
    public void block(long period) {

    }
}
