package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class TaskBehaviour extends Behaviour {
    private boolean completed = false;
    private Task task;

    public TaskBehaviour(BpmnAgent a, Task task) {
        super(a);
        this.task = task;
    }

    @Override
    public void action() {
        if(canRun()) {
            System.out.println("EXECUTION OF TASK " + task.getId());
            this.deactivate(task.getIncoming());
            this.markAsActive(task.getOutgoing());
            completed = true;
            return;
        }
        this.block();
    }

    @Override
    public boolean done() {
        return this.completed;
    }

    private boolean canRun() {
        System.out.println("EXECUTION OF canRun " + task.getId());
        //anyMatch bo to nie bramka
        return task.getIncoming().stream()
                .anyMatch(sequenceFlow -> ((BpmnAgent)myAgent).isActive(sequenceFlow));
    }

    private void markAsActive(Collection<SequenceFlow> outgoingSequenceFlows)
    {
        //TODO filter which should be marked as active?
        ((BpmnAgent)myAgent).markAsActive(outgoingSequenceFlows);
        outgoingSequenceFlows
                .forEach(sequenceFlow -> ((BpmnAgent)myAgent).scheduleActivity(sequenceFlow.getTarget()));
    }

    private void deactivate(Collection<SequenceFlow> sequenceFlows)
    {
        sequenceFlows.forEach(sequenceFlow -> ((BpmnAgent)myAgent).finish(sequenceFlow));
    }
}
