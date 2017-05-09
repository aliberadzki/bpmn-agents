package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class StartEventFactory {
    public static BpmnBehaviour create(StartEvent event, BpmnAgent agent)
    {
        BpmnBehaviour behaviour = null;
        for(EventDefinition ed : event.getEventDefinitions()) {
            String eventType = ed.getElementType().getTypeName();
            if(eventType.equals("messageEventDefinition")) {
                behaviour = new MsgStartBehaviour(agent, event);
            }
            else if(eventType.equals("timerEventDefinition")) {
                behaviour = new TimerStartBehaviour(agent, event);
            }
        }

        if(behaviour == null) behaviour = new PlainStartBehaviour(agent, event);
        return behaviour;
    }
}
