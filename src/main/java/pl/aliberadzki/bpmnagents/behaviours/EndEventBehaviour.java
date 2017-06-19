package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class EndEventBehaviour implements Activity {
    private BpmnAgent bpmnAgent;
    private EndEvent endEvent = null;

    public EndEventBehaviour(BpmnAgent bpmnAgent, EndEvent endEvent)
    {
        this.bpmnAgent = bpmnAgent;
        this.endEvent = endEvent;
    }

    @Override
    public boolean isReady()
    {
        return bpmnAgent.anyIncomingRouteActive(endEvent);
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("EXECUTION OF END_EVENT " + endEvent.getId());
        return true;
    }

    @Override
    public void afterFinish() {

    }

    @Override
    public void block(long period) {

    }

}
