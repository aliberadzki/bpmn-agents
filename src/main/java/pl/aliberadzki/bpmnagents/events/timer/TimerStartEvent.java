package pl.aliberadzki.bpmnagents.events.timer;

import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;
import pl.aliberadzki.bpmnagents.events.EventListener;
import pl.aliberadzki.bpmnagents.events.StartBehaviour;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class TimerStartEvent implements StartBehaviour, Activity, EventListener {

    private BpmnAgent bpmnAgent;
    private Timer timer;

    public TimerStartEvent(BpmnAgent bpmnAgent, TimerEventDefinition timerEventDefinition)
    {
        this.bpmnAgent = bpmnAgent;
        this.timer = new Timer(timerEventDefinition);
    }

    public void onStart()
    {
        timer.start();
    }

    @Override
    public void onEnd() {

    }

    @Override
    public boolean isReady()
    {
        return timer.ticked();
    }

    @Override
    public boolean execute()
    {
        timer.tick();
        bpmnAgent.log("TIMER START BEHAVIOUR FINISHED");
        boolean done = timer.isDone();
        if(!done) {
            this.onStart();
            //blockBehaviour();
        }
        return done;
    }

    @Override
    public void afterFinish() {

    }

    @Override
    public void block(long period)
    {
        bpmnAgent.blockBehaviour(this, period);
    }

}
