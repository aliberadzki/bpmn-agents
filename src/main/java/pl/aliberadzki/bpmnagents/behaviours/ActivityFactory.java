package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class ActivityFactory {
    public static Behaviour create(FlowNode flowNode, BpmnAgent agent) {
        String typeName = flowNode.getElementType().getTypeName();
        switch (typeName) {
            case "task":
                return new TaskBehaviour(agent, (Task)flowNode);

            case "endEvent":
                return new EndEventBehaviour(agent, (EndEvent)flowNode);

            default:
                //IF i dont know what to create, lets just end it
                return new EndEventBehaviour(agent, null);
        }
    }
}
