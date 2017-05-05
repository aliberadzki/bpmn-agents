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
public class MsgStartBehaviour extends SimpleBehaviour implements StartBehaviour{

    private boolean done = false;
    private StartEvent event;

    public MsgStartBehaviour(BpmnAgent a, StartEvent event) {
        super(a);
        this.event = event;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            System.out.println("MSG START BEHAVIOUR FINISHED");
            this.markAsActive(event.getOutgoing());
            this.done = true;
            ((BpmnAgent)myAgent).cleanStartEventBehaviours();
            //TODO: mark outgoing transition as active
        }
    }

    @Override
    public boolean done() {
        return this.done;
    }

    private void markAsActive(Collection<SequenceFlow> outgoingSequenceFlows)
    {
        //TODO filter which should be marked as active?
        ((BpmnAgent)myAgent).markAsActive(outgoingSequenceFlows);
        outgoingSequenceFlows
                .forEach(sequenceFlow -> ((BpmnAgent)myAgent).scheduleActivity(sequenceFlow.getTarget()));

    }
}
