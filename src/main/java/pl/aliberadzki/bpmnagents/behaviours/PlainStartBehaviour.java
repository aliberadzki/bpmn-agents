package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 06.05.17.
 */
public class PlainStartBehaviour extends BpmnBehaviour {
    private BpmnAgent bpmnAgent;
    private final StartEvent startEvent;

    public PlainStartBehaviour(BpmnAgent bpmnAgent, StartEvent event) {
        super(bpmnAgent, event);
        this.bpmnAgent = bpmnAgent;
        this.startEvent = event;
    }

    @Override
    protected boolean canRun() {
        return true;
    }

    @Override
    protected boolean execute() {
        bpmnAgent.log("PLAIN START BEHAVIOUR FINISHED " + startEvent.getId());
        return true;
    }

    @Override
    protected void afterFinish() {
        bpmnAgent.processStarted();
    }
}
