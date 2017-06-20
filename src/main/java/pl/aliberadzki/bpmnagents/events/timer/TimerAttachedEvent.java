package pl.aliberadzki.bpmnagents.events.timer;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.events.AttachedEvent;
import pl.aliberadzki.bpmnagents.events.AttachedEventListener;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class TimerAttachedEvent extends AttachedEvent implements AttachedEventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;
    private final Timer timer;

    public TimerAttachedEvent(BpmnAgent bpmnAgent, BoundaryEvent event, TimerEventDefinition timerEventDefinition)
    {
        super(bpmnAgent, event);
        this.bpmnAgent = bpmnAgent;
        this.event = event;

        this.timer = new Timer(timerEventDefinition);
    }

    public void onStart()
    {
        timer.start();
        bpmnAgent.log("TIMER BOUNDARY EVENT started " + this.event.getName() + " (" + this.event.getId() + ")");
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
        bpmnAgent.log("TIMER BOUNDARY EVENT FIRED " + this.event.getName() + " (" + this.event.getId() + ")");
        boolean done = timer.isDone();
        if(!done) {
            this.onStart();
            //blockBehaviour();
        }
        return done;
    }

    @Override
    public void block(long period) {
        bpmnAgent.blockBehaviour(this, period);
    }
}
