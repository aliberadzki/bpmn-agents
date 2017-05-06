package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class TaskBehaviour extends BpmnBehaviour {
    private Task task;

    public TaskBehaviour(BpmnAgent a, Task task) {
        super(a, task);
        this.task = task;
    }

    @Override
    protected boolean canRun() {
        return anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        System.out.println("EXECUTION OF TASK " + task.getId());
        return true;
    }
}
