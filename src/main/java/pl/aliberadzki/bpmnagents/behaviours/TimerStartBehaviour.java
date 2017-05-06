package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

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
        System.out.println("TIMER START BEHAVIOUR FINISHED");
        return true;
    }

    @Override
    protected void blockBehaviour() {
        super.block(blockTime);
    }

    @Override
    protected void afterFinish() {
        //TODO : maybe it should be possible to conduct 2 parallel instances?
        ((BpmnAgent)myAgent).cleanStartEventBehaviours();
    }
}
