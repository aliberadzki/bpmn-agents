package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class EndEventBehaviour extends BpmnBehaviour {
    private EndEvent endEvent = null;

    public EndEventBehaviour(BpmnAgent agent, EndEvent endEvent) {
        super(agent, endEvent);
        this.endEvent = endEvent;
    }

    @Override
    protected boolean canRun() {
        return this.anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        System.out.println("EXECUTION OF END_EVENT " + endEvent.getId());
        return true;
    }

}
