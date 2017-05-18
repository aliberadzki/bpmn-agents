package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.SendTask;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class SendTaskBehaviour extends TaskBehaviour {
    private BpmnAgent bpmnAgent;
    private SendTask sendTask;

    public SendTaskBehaviour(BpmnAgent bpmnAgent, SendTask sendTask) {
        super(bpmnAgent, sendTask);
        this.bpmnAgent = bpmnAgent;
        this.sendTask = sendTask;
    }

    @Override
    protected boolean execute()
    {
        Collection<AID> receivers = bpmnAgent.findReceivers(sendTask.getId());
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(getInputs().keySet().stream().reduce("|", String::concat));
        receivers.forEach(msg::addReceiver);
        bpmnAgent.send(msg);
        return super.execute();
    }
}
