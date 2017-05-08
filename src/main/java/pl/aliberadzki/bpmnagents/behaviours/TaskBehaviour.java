package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;


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
    public void onStart() {
        task.getModelInstance()
                .getModelElementsByType(BoundaryEvent.class)
                .stream()
                .filter(boundaryEvent -> boundaryEvent.getAttachedTo().getId().equals(task.getId()))
                .forEach(boundaryEvent -> ((BpmnAgent)myAgent).addEventListener(boundaryEvent));
    }

    @Override
    public int onEnd() {
        task.getModelInstance()
                .getModelElementsByType(BoundaryEvent.class)
                .stream()
                .filter(boundaryEvent -> boundaryEvent.getAttachedTo().getId().equals(task.getId()))
                .forEach(boundaryEvent -> ((BpmnAgent)myAgent).cancelEventListener(boundaryEvent.getId()));
        return super.onEnd();
    }

    @Override
    protected boolean canRun() {
        return anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        ((BpmnAgent)myAgent).log("EXECUTION OF TASK " + task.getName() + " (" + task.getId() + ")");
        return true;
    }
}
