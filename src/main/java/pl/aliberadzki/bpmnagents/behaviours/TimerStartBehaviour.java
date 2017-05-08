package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class TimerStartBehaviour extends BpmnBehaviour implements StartBehaviour{

    private StartEvent event;
    private long startTime;
    private long wakeupTime;
    private long period;
    private long blockTime;

    public TimerStartBehaviour(BpmnAgent a, StartEvent event) {
        super(a, event);
        this.event = event;
        this.period = 20000;
    }

    public void onStart() {
        this.startTime = System.currentTimeMillis();
        this.wakeupTime = this.startTime + this.period;
        this.blockTime = this.wakeupTime - System.currentTimeMillis();
    }

    @Override
    protected boolean canRun() {
        long currentTime = System.currentTimeMillis();
        this.blockTime = this.wakeupTime - currentTime;
        return blockTime <= 0L;
    }

    @Override
    protected boolean execute() {
        ((BpmnAgent)myAgent).log("TIMER START BEHAVIOUR FINISHED");
        return true;
    }

    @Override
    protected void blockBehaviour() {
        super.block(blockTime);
    }

    @Override
    protected void afterFinish() {
        ((BpmnAgent)myAgent).cleanStartEventBehaviours();
    }
}
