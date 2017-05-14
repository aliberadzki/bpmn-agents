package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.AID;
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
    protected boolean execute() {
        //TODO send to WHO
        //TODO send WHAT
        Collection<AID> receivers = bpmnAgent.findReciever(sendTask.getId());
        AID receiver = findReceiver("nothing");
        return super.execute();
    }

    private AID findReceiver(String id) {
        return null;
    }
}
