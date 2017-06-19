package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class StartEventFactory {
    public static Activity create(StartEvent event, BpmnAgent agent)
    {
        Activity behaviour = null;
        for(EventDefinition eventDefinition : event.getEventDefinitions()) {
            String eventType = eventDefinition.getElementType().getTypeName();
            if(eventType.equals("messageEventDefinition")) {
                behaviour = new MsgStartBehaviour(agent, event);
            }
            else if(eventType.equals("timerEventDefinition")) {
                behaviour = new TimerStartBehaviour(agent, event, planTimerExecutor(eventDefinition));
            }
        }

        if(behaviour == null) behaviour = new PlainStartBehaviour(agent, event);
        return behaviour;
    }

    private static TimerStrategy planTimerExecutor(EventDefinition eventDefinition)
    {
        return TimerStrategyFactory.create((TimerEventDefinition) eventDefinition);
    }
}
