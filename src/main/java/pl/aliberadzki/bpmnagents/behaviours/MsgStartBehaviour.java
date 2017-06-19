package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;


/**
 * Created by aliberadzki on 04.05.17.
 */
public class MsgStartBehaviour  implements Activity, StartBehaviour {

    private BpmnAgent bpmnAgent;
    private StartEvent event;
    private ACLMessage msg;

    public MsgStartBehaviour(BpmnAgent bpmnAgent, StartEvent event)
    {
        this.bpmnAgent = bpmnAgent;
        this.event = event;
    }

    @Override
    public boolean isReady() {
        this.msg = bpmnAgent.receive();
        return this.msg != null;
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("MSG START BEHAVIOUR FINISHED. msg: " + this.msg.getContent());
        return true;
    }

    public void afterFinish() {
        bpmnAgent.processStarted();
    }

    @Override
    public void block(long period) {

    }
}
