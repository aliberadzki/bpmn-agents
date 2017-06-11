package pl.aliberadzki.bpmnagents.behaviours;

/**
 * Created by aliberadzki on 11.06.17.
 */
public interface TimerStrategy {
    int repeatCount();
    long getPeriod();
}
