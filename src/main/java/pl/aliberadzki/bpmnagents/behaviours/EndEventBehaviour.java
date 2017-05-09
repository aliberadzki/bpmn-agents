package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class EndEventBehaviour extends BpmnBehaviour {
    private BpmnAgent bpmnAgent;
    private EndEvent endEvent = null;

    public EndEventBehaviour(BpmnAgent bpmnAgent, EndEvent endEvent) {
        super(bpmnAgent, endEvent);
        this.bpmnAgent = bpmnAgent;
        this.endEvent = endEvent;
    }

    @Override
    protected boolean canRun() {
        return this.anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        bpmnAgent.log("EXECUTION OF END_EVENT " + endEvent.getId());
        return true;
    }

}
