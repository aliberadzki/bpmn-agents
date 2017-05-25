package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.camunda.bpm.model.bpmn.instance.ReceiveTask;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 14.05.17.
 */
public class ReceiveTaskBehaviour extends TaskBehaviour {
    private BpmnAgent bpmnAgent;
    private ReceiveTask receiveTask;

    public ReceiveTaskBehaviour(BpmnAgent bpmnAgent, ReceiveTask receiveTask) {
        super(bpmnAgent, receiveTask);
        this.bpmnAgent = bpmnAgent;
        this.receiveTask = receiveTask;
    }

    @Override
    protected boolean canRun() {
        return super.canRun();
    }

    @Override
    protected boolean execute()
    {
        ACLMessage message = bpmnAgent.receive(getMessageTemplate());
        if(message != null) {
            bpmnAgent.log("Odebralem: " + message.getContent());
            return super.execute();
        }
        block();
        return false;
    }

    private MessageTemplate getMessageTemplate()
    {
        return MessageTemplate.MatchConversationId(bpmnAgent.getConversationId(receiveTask));
    }
}
