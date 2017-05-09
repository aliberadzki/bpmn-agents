package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;


/**
 * Created by aliberadzki on 07.05.17.
 */
public class WaitingTaskBehaviour extends TaskBehaviour {
    private BpmnAgent bpmnAgent;
    private final Task task;
    private final long period = 20000;
    private long wakeupTime = -1;

    public WaitingTaskBehaviour(BpmnAgent bpmnAgent, Task task) {
        super(bpmnAgent, task);
        this.bpmnAgent = bpmnAgent;
        this.task = task;
    }

    @Override
    protected boolean execute() {
        long current = System.currentTimeMillis();
        if(wakeupTime < 0L) {
            this.wakeupTime = current + period;
            this.block(period);
            return false;
        }
        if(current < wakeupTime) {
            this.block(wakeupTime - current);
            return false;
        }
        bpmnAgent.log("WAITING TASK FINISHED " + task.getName() + " (" + task.getId() + ")");
        return true;
    }
}