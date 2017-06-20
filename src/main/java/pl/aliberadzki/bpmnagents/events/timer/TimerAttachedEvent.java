package pl.aliberadzki.bpmnagents.events.timer;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.events.AttachedEvent;
import pl.aliberadzki.bpmnagents.events.AttachedEventListener;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class TimerAttachedEvent extends AttachedEvent implements AttachedEventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;
    private final long period;
    private long startTime;
    private long wakeupTime;
    private long blockTime;

    public TimerAttachedEvent(BpmnAgent bpmnAgent, BoundaryEvent event, long period)
    {
        super(bpmnAgent, event);
        this.bpmnAgent = bpmnAgent;
        this.event = event;
        this.period = period;
    }

    public void onStart()
    {
        this.startTime = System.currentTimeMillis();
        this.wakeupTime = this.startTime + this.period;
        this.blockTime = this.wakeupTime - this.startTime;
    }

    @Override
    public void onEnd() {

    }

    @Override
    public boolean isReady()
    {
        long currentTime = System.currentTimeMillis();
        this.blockTime = this.wakeupTime - currentTime;
        return blockTime <= 0L;
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("TIMER BOUNDARY EVENT FIRED " + this.event.getName() + " (" + this.event.getId() + ")");
        return true;
    }

    @Override
    public void afterFinish() {

    }

    @Override
    public void block(long period) {

    }

//    protected void blockBehaviour() {
//        super.block(blockTime);
//    }
}
