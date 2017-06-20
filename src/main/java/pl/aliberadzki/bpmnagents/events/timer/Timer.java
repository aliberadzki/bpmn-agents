package pl.aliberadzki.bpmnagents.events.timer;

import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.events.EventListener;

/**
 * Created by aliberadzki on 20.06.17.
 */
public class Timer {
    private TimerStrategy strategy;
    private long startTime;
    private long wakeupTime;
    private long blockTime;
    private long cycles = 0;

    public Timer(TimerEventDefinition eventDefinition)
    {
        this.strategy = TimerStrategyFactory.create(eventDefinition);
    }

    public void setStrategy(TimerStrategy strategy)
    {
        this.strategy = strategy;
    }

    public void start()
    {
        this.startTime = System.currentTimeMillis();
        this.wakeupTime = this.startTime + strategy.getPeriod();
        this.blockTime = this.wakeupTime - System.currentTimeMillis();
    }

    public boolean ticked()
    {
        long currentTime = System.currentTimeMillis();
        this.blockTime = this.wakeupTime - currentTime;
        return blockTime <= 0L;
    }

    public boolean isDone()
    {
        return this.cycles >= strategy.repeatCount();
    }

    public void tick()
    {
        this.cycles++;
    }
}
