package pl.aliberadzki.bpmnagents.interpreter;

import org.camunda.bpm.model.bpmn.instance.Event;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;
import pl.aliberadzki.bpmnagents.activities.ActivityFactory;
import pl.aliberadzki.bpmnagents.behaviours.*;
import pl.aliberadzki.bpmnagents.events.AttachedEvent;
import pl.aliberadzki.bpmnagents.events.StartEventFactory;

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
        Activity activity = ActivityFactory.create(event, myAgent);
        if(activity == null) return;
        BpmnBehaviour behaviour = new BpmnBehaviour(myAgent, event, activity);
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
                .filter(AttachedEvent.class::isInstance)
                .filter(bpmnBehaviour -> isAttachedTo(activityId, bpmnBehaviour))
                .collect(Collectors.toList());
        collect.forEach(bpmBeh -> cancelEventListener(bpmBeh.getId()));
    }

    private boolean isAttachedTo(String activityId, BpmnBehaviour bpmnBehaviour)
    {
        return ((AttachedEvent)bpmnBehaviour.getActivity())
                .getAttachedToId()
                .equals(activityId);
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
        Activity activity = StartEventFactory.create(startEvent, myAgent);
        if(activity == null) return;
        BpmnBehaviour behaviour = new BpmnBehaviour(myAgent, startEvent, activity);
        myAgent.addBehaviour(behaviour);
        this.startBehaviours.add(behaviour);
    }
}
