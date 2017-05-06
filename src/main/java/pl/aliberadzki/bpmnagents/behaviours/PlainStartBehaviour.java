package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 06.05.17.
 */
public class PlainStartBehaviour extends BpmnBehaviour {
    private final StartEvent startEvent;

    public PlainStartBehaviour(BpmnAgent agent, StartEvent event) {
        super(agent, event);
        this.startEvent = event;
    }

    @Override
    protected boolean canRun() {
        return true;
    }

    @Override
    protected boolean execute() {
        System.out.println("PLAIN START BEHAVIOUR FINISHED " + startEvent.getId());
        return true;
    }

    @Override
    protected void afterFinish() {
        //TODO : maybe it should be possible to conduct 2 parallel instances?
        ((BpmnAgent)myAgent).cleanStartEventBehaviours();
    }
}
