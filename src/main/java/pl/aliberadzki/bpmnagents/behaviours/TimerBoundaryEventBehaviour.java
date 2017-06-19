package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class TimerBoundaryEventBehaviour implements AttachedEventListener {
    private BpmnAgent bpmnAgent;
    private final BoundaryEvent event;
    private final long period;
    private long startTime;
    private long wakeupTime;
    private long blockTime;

    public TimerBoundaryEventBehaviour(BpmnAgent bpmnAgent, BoundaryEvent event, long period)
    {
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
