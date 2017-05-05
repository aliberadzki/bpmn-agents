package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class StartEventFactory {
    public static Behaviour create(StartEvent event, BpmnAgent agent)
    {
        Behaviour behaviour = null;
        for(EventDefinition ed : event.getEventDefinitions()) {
            String eventType = ed.getElementType().getTypeName();
            if(eventType.equals("messageEventDefinition")) {
                behaviour = new MsgStartBehaviour(agent, event);
            }
            else if(eventType.equals("timerEventDefinition")) {
                behaviour = new TimerStartBehaviour(agent, event);
            }
        }
        return behaviour;
    }
}
