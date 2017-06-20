package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.behaviours.TaskBehaviour;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class Wait implements Action {
    private long period;
    private long wakeupTime;
    private TaskBehaviour taskBehaviour;

    public Wait(TaskBehaviour taskBehaviour) {
        this.taskBehaviour = taskBehaviour;
        this.period = 10000;
        this.wakeupTime = System.currentTimeMillis() + this.period;
    }

    @Override
    public boolean execute() {
        taskBehaviour.log("WAITING ACTION STARTED");
        long current = System.currentTimeMillis();
        if(wakeupTime < 0L) {
            this.wakeupTime = current + period;
            taskBehaviour.block(period);
            return false;
        }
        if(current < wakeupTime) {
            taskBehaviour.block(wakeupTime - current);
            return false;
        }
        taskBehaviour.log("WAITING ACTION FINISHED");
        return true;
    }
}
