package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;


/**
 * Created by aliberadzki on 05.05.2017.
 */
public class TaskBehaviour extends BpmnBehaviour {
    private BpmnAgent bpmnAgent;
    private Task task;

    public TaskBehaviour(BpmnAgent bpmnAgent, Task task) {
        super(bpmnAgent, task);
        this.bpmnAgent = bpmnAgent;
        this.task = task;
    }

    @Override
    public void onStart()
    {
        bpmnAgent.addBoundaryEventsFor(task);
    }

    @Override
    public int onEnd() {
        bpmnAgent.clearBoundaryEventsFor(task);
        return super.onEnd();
    }

    @Override
    protected boolean canRun() {
        return anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        bpmnAgent.log("EXECUTION OF TASK " + task.getName() + " (" + task.getId() + ")");
        return true;
    }
}
