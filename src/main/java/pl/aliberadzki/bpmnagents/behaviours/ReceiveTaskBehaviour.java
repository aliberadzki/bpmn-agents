package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.ReceiveTask;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 14.05.17.
 */
public class ReceiveTaskBehaviour extends TaskBehaviour {
    public ReceiveTaskBehaviour(BpmnAgent bpmnAgent, ReceiveTask receiveTask) {
        super(bpmnAgent, receiveTask);
    }

    @Override
    protected boolean canRun() {
        return super.canRun();
    }

    @Override
    protected boolean execute() {
        return super.execute();
    }
}
