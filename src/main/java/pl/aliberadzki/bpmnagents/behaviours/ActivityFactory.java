package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.*;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import java.util.Collection;

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
                return createBoundary((BoundaryEvent) flowNode, agent);
            case "endEvent":
                return new EndEventBehaviour(agent, (EndEvent)flowNode);

            default:
                //IF i dont know what to create, lets just end it
                return new EndEventBehaviour(agent, null);
        }
    }

    private static BoundaryEventBehaviour createBoundary(BoundaryEvent flowNode, BpmnAgent agent) {
        //TODO: check event definitions
        EventDefinition def= flowNode.getEventDefinitions().iterator().next();
        if(def.getElementType().getTypeName() == "timerEventDefinition") {
            return new TimerBoundaryEventBehaviour(agent, flowNode);
        }
        return new MsgBoundaryEventBehaviour(agent, flowNode);
    }
}
