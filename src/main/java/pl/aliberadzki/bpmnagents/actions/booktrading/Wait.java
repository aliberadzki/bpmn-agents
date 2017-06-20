package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.activities.TaskActivity;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class Wait implements Action {
    private long period;
    private long wakeupTime;
    private TaskActivity taskActivity;

    public Wait(TaskActivity taskActivity) {
        this.taskActivity = taskActivity;
        this.period = 10000;
        this.wakeupTime = System.currentTimeMillis() + this.period;
    }

    @Override
    public boolean execute() {
        long current = System.currentTimeMillis();
        if(wakeupTime < 0L) {
            this.wakeupTime = current + period;
            taskActivity.block(period);
            return false;
        }
        if(current < wakeupTime) {
            taskActivity.block(wakeupTime - current);
            return false;
        }
        taskActivity.log("WAITING ACTION FINISHED");
        return true;
    }
}
