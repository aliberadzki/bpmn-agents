package pl.aliberadzki.bpmnagents.activities;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.SendTask;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class SendTaskActivity extends TaskActivity {
    private BpmnAgent bpmnAgent;
    private SendTask sendTask;

    public SendTaskActivity(BpmnAgent bpmnAgent, SendTask sendTask)
    {
        super(bpmnAgent, sendTask);
        this.bpmnAgent = bpmnAgent;
        this.sendTask = sendTask;
    }

    @Override
    public boolean execute()
    {
        ACLMessage msg = createACLMessage();
        bpmnAgent.log("Wysyłam: " + msg.getContent());
        bpmnAgent.send(msg);
        return super.execute();
    }

    private ACLMessage createACLMessage()
    {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setConversationId(bpmnAgent.getConversationId(sendTask));
        msg.setContent(getMsgContent());
        bpmnAgent.findReceivers(sendTask)
                .forEach(msg::addReceiver);
        return msg;
    }

    private String getMsgContent()
    {
        return bpmnAgent.generateMsgContentFromInputs(getInputs());

    }
}
