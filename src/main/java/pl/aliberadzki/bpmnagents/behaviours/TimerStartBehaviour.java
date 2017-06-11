package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class TimerStartBehaviour extends BpmnBehaviour implements StartBehaviour{

    private BpmnAgent bpmnAgent;
    private StartEvent event;
    private long startTime;
    private long wakeupTime;
    private long period;
    private long blockTime;
    private long cycles =0;
    private long plannedCycles;

    public TimerStartBehaviour(BpmnAgent bpmnAgent, StartEvent event, TimerStrategy timerStrategy)
    {
        super(bpmnAgent, event);
        this.bpmnAgent = bpmnAgent;
        this.event = event;
        this.period = timerStrategy.getPeriod();
        this.plannedCycles = timerStrategy.repeatCount();
    }

    public void onStart()
    {
        this.startTime = System.currentTimeMillis();
        this.wakeupTime = this.startTime + this.period;
        this.blockTime = this.wakeupTime - System.currentTimeMillis();
    }

    @Override
    protected boolean canRun()
    {
        long currentTime = System.currentTimeMillis();
        this.blockTime = this.wakeupTime - currentTime;
        return blockTime <= 0L;
    }

    @Override
    public void action()
    {
        if(canRun()) {
            this.beforeFinish();
            this.done = this.execute();
            if(done) {
                this.activate(getOutgoing());
                this.afterFinish();
            }
        }
        else blockBehaviour();
    }

    @Override
    protected boolean execute()
    {
        this.cycles++;
        bpmnAgent.log("TIMER START BEHAVIOUR FINISHED");
        boolean done = this.cycles >= this.plannedCycles;
        if(!done) {
            this.onStart();
            blockBehaviour();
        }
        return done;
    }

    @Override
    protected void blockBehaviour() {
        super.block(blockTime);
    }

    @Override
    protected void afterFinish() {
        //bpmnAgent.processStartedAndAnotherComing(this);
    }
}
