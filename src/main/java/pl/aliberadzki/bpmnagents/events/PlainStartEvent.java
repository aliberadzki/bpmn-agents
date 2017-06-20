package pl.aliberadzki.bpmnagents.events;

import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;

/**
 * Created by aliberadzki on 06.05.17.
 */
public class PlainStartEvent implements StartBehaviour, Activity {
    private BpmnAgent bpmnAgent;
    private final StartEvent startEvent;

    public PlainStartEvent(BpmnAgent bpmnAgent, StartEvent event)
    {
        this.bpmnAgent = bpmnAgent;
        this.startEvent = event;
    }

    @Override
    public boolean isReady()
    {
        return true;
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("PLAIN START BEHAVIOUR FINISHED " + startEvent.getId());
        return true;
    }

    @Override
    public void afterFinish()
    {
        bpmnAgent.processStarted();
    }

    @Override
    public void block(long period) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }
}
