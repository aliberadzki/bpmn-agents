package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 07.05.17.
 */
public class TimerBoundaryEventBehaviour extends BoundaryEventBehaviour {
    private final BoundaryEvent event;
    private final long period;
    private long startTime;
    private long wakeupTime;
    private long blockTime;

    public TimerBoundaryEventBehaviour(BpmnAgent agent, BoundaryEvent event)
    {
        super(agent, event);
        this.event = event;
        this.period = Long.valueOf(this.getTimerDuration());
    }

    @Override
    public void onStart()
    {
        this.startTime = System.currentTimeMillis();
        this.wakeupTime = this.startTime + this.period;
        this.blockTime = this.wakeupTime - this.startTime;
    }

    @Override
    protected boolean canRun()
    {
        long currentTime = System.currentTimeMillis();
        this.blockTime = this.wakeupTime - currentTime;
        return blockTime <= 0L;
    }

    @Override
    protected boolean execute()
    {
        ((BpmnAgent)myAgent).log("TIMER BOUNDARY EVENT FIRED " + this.event.getName() + " (" + this.event.getId() + ")");
        return true;
    }

    @Override
    protected void blockBehaviour() {
        super.block(blockTime);
    }

    private String getTimerDuration()
    {
        return ((TimerEventDefinition)this.event.getEventDefinitions().iterator().next())
                .getTimeDuration().getTextContent();
    }
}
