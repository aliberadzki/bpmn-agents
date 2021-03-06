package pl.aliberadzki.bpmnagents.activities;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.camunda.bpm.model.bpmn.instance.ReceiveTask;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.knowledge.Belief;
import pl.aliberadzki.bpmnagents.knowledge.Knowledge;

import java.util.Collection;

/**
 * Created by aliberadzki on 14.05.17.
 */
public class ReceiveTaskActivity extends TaskActivity {
    private BpmnAgent bpmnAgent;
    private ReceiveTask receiveTask;

    public ReceiveTaskActivity(BpmnAgent bpmnAgent, ReceiveTask receiveTask) {
        super(bpmnAgent, receiveTask);
        this.bpmnAgent = bpmnAgent;
        this.receiveTask = receiveTask;
    }

    @Override
    public boolean execute()
    {
        ACLMessage message = bpmnAgent.receive(getMessageTemplate());
        if(message != null) {
            bpmnAgent.log("Odebralem: " + message.getContent());
            //TODO: read content and set output
            Collection<Belief> beliefs = Knowledge.parseBeliefs(message.getContent());
            beliefs.forEach(belief -> this.setOutput(belief.getName(), belief.getValue()));
            return super.execute();
        }
        return false;
    }

    private MessageTemplate getMessageTemplate()
    {
        return MessageTemplate.MatchConversationId(bpmnAgent.getConversationId(receiveTask));
    }
}
