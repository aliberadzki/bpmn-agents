package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

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
    protected void beforeFinish() {

    }

    @Override
    protected void afterFinish() {
            ((BpmnAgent)myAgent).cleanStartEventBehaviours();
    }
}
