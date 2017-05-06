package pl.aliberadzki.bpmnagents.behaviours;

import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;


/**
 * Created by aliberadzki on 04.05.17.
 */
public class MsgStartBehaviour extends BpmnBehaviour implements StartBehaviour{

    private StartEvent event;

    public MsgStartBehaviour(BpmnAgent a, StartEvent event) {
        super(a, event);
        this.event = event;
    }

    @Override
    protected boolean canRun() {
        ACLMessage msg = myAgent.receive();
        return msg != null;
    }

    @Override
    protected boolean execute() {
        System.out.println("MSG START BEHAVIOUR FINISHED");
        return true;
    }

    @Override
    protected void afterFinish() {
        //TODO : maybe it should be possible to conduct 2 parallel instances?
        ((BpmnAgent)myAgent).cleanStartEventBehaviours();
    }
}
