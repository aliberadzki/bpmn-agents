package pl.aliberadzki.bpmnagents.activities;

import org.camunda.bpm.model.bpmn.instance.*;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.behaviours.*;
import pl.aliberadzki.bpmnagents.events.AttachedEventListener;
import pl.aliberadzki.bpmnagents.events.PlainEndEvent;
import pl.aliberadzki.bpmnagents.events.message.MsgAttachedEvent;
import pl.aliberadzki.bpmnagents.events.timer.TimerAttachedEvent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class ActivityFactory {
    public static Activity create(FlowNode flowNode, BpmnAgent agent)
    {
        String typeName = flowNode.getElementType().getTypeName();
        switch (typeName) {
            case "task":
                return createTask(flowNode, agent);
            case "boundaryEvent":
                return createBoundary((BoundaryEvent) flowNode, agent);
            case "endEvent":
                return new PlainEndEvent(agent, (EndEvent)flowNode);
            case "exclusiveGateway":
                return new ExclusiveGatewayBehaviour(agent, (ExclusiveGateway)flowNode);
            case "sendTask":
                return new SendTaskActivity(agent, (SendTask)flowNode);
            case "receiveTask":
                return new ReceiveTaskActivity(agent, (ReceiveTask)flowNode);

            default:
                return new PlainEndEvent(agent, null);
        }
    }

    private static Activity createTask(FlowNode flowNode, BpmnAgent agent)
    {
        return new TaskActivity(agent, (Task)flowNode);
    }

    private static AttachedEventListener createBoundary(BoundaryEvent flowNode, BpmnAgent agent)
    {
        EventDefinition def= flowNode.getEventDefinitions().iterator().next();
        if(isTimerEvent(def)) {
            long period = getTimerLength((TimerEventDefinition) def);
            return new TimerAttachedEvent(agent, flowNode, period);
        }
        Message msg = getMessage(flowNode);
        return new MsgAttachedEvent(agent, flowNode, msg);
    }

    private static Message getMessage(BoundaryEvent flowNode)
    {
        return ((MessageEventDefinition)flowNode
                .getEventDefinitions()
                .iterator().next())
                .getMessage();
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
