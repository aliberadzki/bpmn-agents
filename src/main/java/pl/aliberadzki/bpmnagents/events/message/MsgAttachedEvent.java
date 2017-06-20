package pl.aliberadzki.bpmnagents.events.message;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.Message;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.events.AttachedEvent;
import pl.aliberadzki.bpmnagents.events.AttachedEventListener;
import pl.aliberadzki.bpmnagents.events.EventListener;

import java.util.Optional;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class MsgAttachedEvent extends AttachedEvent implements EventListener, AttachedEventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;
    private Message msg;
    private ACLMessage aclMessage = null;

    public MsgAttachedEvent(BpmnAgent bpmnAgent, BoundaryEvent event, Message msg)
    {
        super(bpmnAgent, event);
        this.bpmnAgent = bpmnAgent;
        this.event = event;
        this.msg = msg;
    }

    @Override
    public boolean isReady()
    {
        this.aclMessage = receiveMatchingMsg();
        return this.aclMessage != null;
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("Boundary event ran! (aclMessage: "
                + this.aclMessage.getContent()
                + ") cancelActivity: "
                + event.cancelActivity());
        return true;
    }

    @Override
    public void block(long period)
    {
        bpmnAgent.blockBehaviour(this, period);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    private ACLMessage receiveMatchingMsg()
    {
        if(msg==null) return bpmnAgent.receive();
        return bpmnAgent.receive(MessageTemplate.MatchConversationId(msg.getId()));
    }
}
