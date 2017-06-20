package pl.aliberadzki.bpmnagents.events;

import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;
import pl.aliberadzki.bpmnagents.events.message.MsgStartEvent;
import pl.aliberadzki.bpmnagents.events.timer.TimerStartEvent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class StartEventFactory {
    public static Activity create(StartEvent event, BpmnAgent agent)
    {
        for(EventDefinition eventDefinition : event.getEventDefinitions()) {
            String eventType = eventDefinition.getElementType().getTypeName();
            if(eventType.equals("messageEventDefinition")) {
                return new MsgStartEvent(agent, event);
            }
            if(eventType.equals("timerEventDefinition")) {
                return new TimerStartEvent(agent, (TimerEventDefinition) event);
            }
        }
        return new PlainStartEvent(agent, event);
    }
}
