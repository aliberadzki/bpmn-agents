package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;


/**
 * Created by aliberadzki on 04.05.17.
 */
public class MsgStartBehaviour extends BpmnBehaviour implements StartBehaviour{

    private BpmnAgent bpmnAgent;
    private StartEvent event;
    private ACLMessage msg;

    public MsgStartBehaviour(BpmnAgent bpmnAgent, StartEvent event) {
        super(bpmnAgent, event);
        this.bpmnAgent = bpmnAgent;
        this.event = event;
    }

    @Override
    protected boolean canRun() {
        this.msg = myAgent.receive();
        return this.msg != null;
    }

    @Override
    protected boolean execute() {
        ((BpmnAgent)myAgent).log("MSG START BEHAVIOUR FINISHED. msg: " + this.msg.getContent());
        return true;
    }

    @Override
    protected void afterFinish() {
        bpmnAgent.processStarted();
    }
}
