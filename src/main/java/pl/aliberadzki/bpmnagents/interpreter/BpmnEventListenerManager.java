package pl.aliberadzki.bpmnagents.interpreter;

import org.camunda.bpm.model.bpmn.instance.Event;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.behaviours.ActivityFactory;
import pl.aliberadzki.bpmnagents.behaviours.BoundaryEventBehaviour;
import pl.aliberadzki.bpmnagents.behaviours.BpmnBehaviour;
import pl.aliberadzki.bpmnagents.behaviours.StartEventFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aliberadzki on 09.05.2017.
 */
public class BpmnEventListenerManager {

    private Collection<BpmnBehaviour> startBehaviours = new ArrayList<>();
    private Collection<BpmnBehaviour> eventListeners = new ArrayList<>();
    private BpmnAgent myAgent;
    private Process process;

    public BpmnEventListenerManager(BpmnAgent myAgent, Process process)
    {
        this.myAgent = myAgent;
        this.process = process;
        this.initStartEvents();
        //TODO init intermediate events WHEN process started
    }

    public void cleanStartEventBehaviours()
    {
        this.startBehaviours.forEach(myAgent::removeBehaviour);
        this.startBehaviours.clear();
    }

    public void addEventListener(Event event)
    {
        BpmnBehaviour behaviour = ActivityFactory.create(event, myAgent);
        if(behaviour == null) return;
        myAgent.addBehaviour(behaviour);
        this.eventListeners.add(behaviour);
    }


    public void cancelEventListener(String eventId)
    {
        BpmnBehaviour behaviour = this.eventListeners.stream()
                .filter(bpmnBehaviour -> bpmnBehaviour.getId().equals(eventId))
                .findFirst()
                .orElse(null);

        if(behaviour == null ) return;
        myAgent.log("Cancelling listener : " + eventId);
        myAgent.removeBehaviour(behaviour);
        this.eventListeners.remove(behaviour);
    }


    public void initStartEvents()
    {
        Collection<StartEvent> events = getStartEvents(this.process);
        events.forEach(this::addStartEventListener);
    }


    public void cancelEventListenersOn(String activityId)
    {
        List<BpmnBehaviour> collect = this.eventListeners.stream()
                .filter(BoundaryEventBehaviour.class::isInstance)
                .filter(bpmnBehaviour -> isAttachedTo(activityId, bpmnBehaviour))
                .collect(Collectors.toList());
        collect.forEach(bpmBeh -> cancelEventListener(bpmBeh.getId()));
    }

    private boolean isAttachedTo(String activityId, BpmnBehaviour bpmnBehaviour)
    {
        return ((BoundaryEventBehaviour)bpmnBehaviour).getAttachedToId().equals(activityId);
    }

    private Collection<StartEvent> getStartEvents(Process process)
    {
        return process.getFlowElements().stream()
                .filter(StartEvent.class::isInstance)
                .map(StartEvent.class::cast)
                .collect(Collectors.toList());
    }

    private void addStartEventListener(StartEvent startEvent)
    {
        BpmnBehaviour behaviour = StartEventFactory.create(startEvent, myAgent);
        if(behaviour == null) return;
        myAgent.addBehaviour(behaviour);
        this.startBehaviours.add(behaviour);
    }
}
