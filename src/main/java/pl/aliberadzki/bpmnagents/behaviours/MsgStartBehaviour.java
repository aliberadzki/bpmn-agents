package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class MsgStartBehaviour extends SimpleBehaviour {

    private boolean done = false;

    public MsgStartBehaviour(BpmnAgent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            System.out.println("MSG START BEHAVIOUR FINISHED");
            this.done = true;
            ((BpmnAgent)myAgent).cleanStartEventBehaviours();
        }
    }

    @Override
    public boolean done() {
        return this.done;
    }


}
