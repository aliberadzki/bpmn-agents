package pl.aliberadzki.bpmnagents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import pl.aliberadzki.bpmnagents.behaviours.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class BpmnAgent extends Agent {
    private String bpdName;
    private String participantId;

    private Collection<BpmnBehaviour> startBehaviours = new ArrayList<>();
    private Collection<BpmnBehaviour> eventListeners = new ArrayList<>();
    private Collection<BpmnBehaviour> activityBehaviours = new ArrayList<>();
    private Collection<SequenceFlow> activeFlows = new ArrayList<>();
    private Collection<SequenceFlow> finishedFlows = new ArrayList<>();

    @Override
    protected void setup()
    {
        this.bpdName = (String) getArguments()[0];
        this.participantId = (String) getArguments()[1];
        this.initStartEvents();
        //TODO init intermediate events WHEN process started
    }

    public void cleanStartEventBehaviours()
    {
        this.startBehaviours.forEach(this::removeBehaviour);
    }

    private void initStartEvents()
    {
        Collection<StartEvent> events = getStartEvent(this.getProcess());
        events.forEach(this::addStartEventListener);
    }

    private Collection<StartEvent> getStartEvent(Process process)
    {
        return process.getFlowElements().stream()
                .filter(StartEvent.class::isInstance)
                .map(StartEvent.class::cast)
                .collect(Collectors.toList());
    }

    private Process getProcess()
    {
        InputStream bpdStream = getClass()
                .getClassLoader()
                .getResourceAsStream(bpdName + ".bpmn");
        BpmnModelInstance modelInstance = bpdStream!=null
                ? Bpmn.readModelFromStream(bpdStream)
                : Bpmn.createEmptyModel();
        String processRef = modelInstance
                .getModelElementById(participantId)
                .getAttributeValue("processRef");

        return modelInstance.getModelElementById(processRef);
    }

    public void markAsActive(Collection<SequenceFlow> sequenceFlows)
    {
        activeFlows.addAll(sequenceFlows);
    }

    public boolean isActive(SequenceFlow sequenceFlow) {
        return activeFlows.contains(sequenceFlow);
    }

    public void finish(SequenceFlow flow)
    {
        this.activeFlows.removeIf(flow1 -> flow1.equals(flow));
        this.finishedFlows.add(flow);
    }

    private void addStartEventListener(StartEvent startEvent)
    {
        BpmnBehaviour behaviour = StartEventFactory.create(startEvent, this);
        if(behaviour == null) return;
        this.addBehaviour(behaviour);
        this.startBehaviours.add(behaviour);
    }

    public void addEventListener(Event event)
    {
        BpmnBehaviour behaviour = ActivityFactory.create(event, this);
        if(behaviour == null) return;
        this.addBehaviour(behaviour);
        this.eventListeners.add(behaviour);
    }

    public void scheduleActivity(FlowNode flowNode)
    {
        BpmnBehaviour behaviour = ActivityFactory.create(flowNode, this);
        if(behaviour == null) return;
        this.addBehaviour(behaviour);
        this.activityBehaviours.add(behaviour);
    }

    public void cancelBehaviour(String activityId) {
        BpmnBehaviour behaviour = this.activityBehaviours.stream()
                .filter(bpmnBehaviour -> bpmnBehaviour.getId().equals(activityId))
                .findFirst()
                .orElse(null);

        if(behaviour == null ) return;
        System.out.println("Cancelling behaviour : " + activityId);
        this.removeBehaviour(behaviour);
        this.activityBehaviours.remove(behaviour);
    }

    public void cancelEventListener(String eventId) {
        BpmnBehaviour behaviour = this.eventListeners.stream()
                .filter(bpmnBehaviour -> bpmnBehaviour.getId().equals(eventId))
                .findFirst()
                .orElse(null);

        if(behaviour == null ) return;
        System.out.println("Cancelling listener : " + eventId);
        this.removeBehaviour(behaviour);
        this.eventListeners.remove(behaviour);
    }
}
