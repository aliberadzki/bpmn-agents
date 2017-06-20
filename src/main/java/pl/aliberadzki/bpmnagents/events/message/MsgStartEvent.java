package pl.aliberadzki.bpmnagents.events.message;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;
import pl.aliberadzki.bpmnagents.events.StartBehaviour;


/**
 * Created by aliberadzki on 04.05.17.
 */
public class MsgStartEvent implements Activity, StartBehaviour {

    private BpmnAgent bpmnAgent;
    private StartEvent event;
    private ACLMessage msg;

    public MsgStartEvent(BpmnAgent bpmnAgent, StartEvent event)
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

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }
}
