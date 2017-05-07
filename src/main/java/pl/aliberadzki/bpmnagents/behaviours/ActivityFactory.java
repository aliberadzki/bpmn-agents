package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class ActivityFactory {
    public static BpmnBehaviour create(FlowNode flowNode, BpmnAgent agent) {
        String typeName = flowNode.getElementType().getTypeName();
        switch (typeName) {
            case "task":
                //TODO this if should be removed
                if(flowNode.getName().contains("wait")) return new WaitingTaskBehaviour(agent, (Task)flowNode);
                return new TaskBehaviour(agent, (Task)flowNode);

            case "boundaryEvent":
                return new BoundaryEventBehaviour(agent, (BoundaryEvent)flowNode);
            case "endEvent":
                return new EndEventBehaviour(agent, (EndEvent)flowNode);

            default:
                //IF i dont know what to create, lets just end it
                return new EndEventBehaviour(agent, null);
        }
    }
}
