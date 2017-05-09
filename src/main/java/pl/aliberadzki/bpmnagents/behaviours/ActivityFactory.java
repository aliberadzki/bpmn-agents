package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.*;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import java.util.Objects;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class ActivityFactory {
    public static BpmnBehaviour create(FlowNode flowNode, BpmnAgent agent) {
        String typeName = flowNode.getElementType().getTypeName();
        switch (typeName) {
            case "task":
                return createTask(flowNode, agent);

            case "boundaryEvent":
                return createBoundary((BoundaryEvent) flowNode, agent);
            case "endEvent":
                return new EndEventBehaviour(agent, (EndEvent)flowNode);
            case "exclusiveGateway":
                return new ExclusiveGatewayBehaviour(agent, (ExclusiveGateway)flowNode);

            default:
                //IF i dont know what to create, lets just end it
                return new EndEventBehaviour(agent, null);
        }
    }

    private static BpmnBehaviour createTask(FlowNode flowNode, BpmnAgent agent) {
        if(flowNode.getName().contains("wait")) return new WaitingTaskBehaviour(agent, (Task)flowNode);
        return new TaskBehaviour(agent, (Task)flowNode);
    }

    private static BoundaryEventBehaviour createBoundary(BoundaryEvent flowNode, BpmnAgent agent) {
        EventDefinition def= flowNode.getEventDefinitions().iterator().next();
        if(Objects.equals(def.getElementType().getTypeName(), "timerEventDefinition")) {
            //TODO it is ugly
            long period = Long.valueOf(((TimerEventDefinition)def).getTimeDuration().getTextContent());
            return new TimerBoundaryEventBehaviour(agent, flowNode, period);
        }
        return new MsgBoundaryEventBehaviour(agent, flowNode);
    }
}
