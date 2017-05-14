package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.*;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class ActivityFactory {
    public static BpmnBehaviour create(FlowNode flowNode, BpmnAgent agent)
    {
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
            case "sendTask":
                return new SendTaskBehaviour(agent, (SendTask)flowNode);
            case "receiveTask":
                return new ReceiveTaskBehaviour(agent, (ReceiveTask)flowNode);

            default:
                return new EndEventBehaviour(agent, null);
        }
    }

    private static BpmnBehaviour createTask(FlowNode flowNode, BpmnAgent agent)
    {
        return new TaskBehaviour(agent, (Task)flowNode);
    }

    private static BoundaryEventBehaviour createBoundary(BoundaryEvent flowNode, BpmnAgent agent)
    {
        EventDefinition def= flowNode.getEventDefinitions().iterator().next();
        if(isTimerEvent(def)) {
            long period = getTimerLength((TimerEventDefinition) def);
            return new TimerBoundaryEventBehaviour(agent, flowNode, period);
        }
        Message msg = getMessage(flowNode);
        return new MsgBoundaryEventBehaviour(agent, flowNode, msg);
    }

    private static Message getMessage(BoundaryEvent flowNode)
    {
        return ((MessageEventDefinition)flowNode.getEventDefinitions().iterator().next()).getMessage();
    }

    private static Long getTimerLength(TimerEventDefinition def)
    {
        return Long.valueOf(def.getTimeDuration().getTextContent());
    }

    private static boolean isTimerEvent(EventDefinition def)
    {
        return "timerEventDefinition".equals(def.getElementType().getTypeName());
    }
}
