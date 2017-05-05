package pl.aliberadzki.bpmnagents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.behaviours.ActivityFactory;
import pl.aliberadzki.bpmnagents.behaviours.StartBehaviour;
import pl.aliberadzki.bpmnagents.behaviours.StartEventFactory;

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

    private Collection<Behaviour> startBehaviours = new ArrayList<>();
    private Collection<SequenceFlow> activeFlows = new ArrayList<>();
    private Collection<SequenceFlow> finishedFlows = new ArrayList<>();

    @Override
    protected void setup()
    {
        this.bpdName = (String) getArguments()[0];
        this.participantId = (String) getArguments()[1];
        this.initStartEventListeners();
        //TODO init intermediate start events WHEN process started
    }

    @Override
    public void addBehaviour(Behaviour b) {
        super.addBehaviour(b);
        if(b instanceof StartBehaviour) {
            this.startBehaviours.add(b);
        }
    }

    public void cleanStartEventBehaviours()
    {
        this.startBehaviours.forEach(this::removeBehaviour);
    }

    private void initStartEventListeners()
    {
        getStartEvent(this.getProcess())
                .forEach(this::addStartEventListener);
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

    private void addStartEventListener(StartEvent startEvent)
    {
        Behaviour behaviour = StartEventFactory.create(startEvent, this);
        if(behaviour == null) return;
        this.addBehaviour(behaviour);
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

    public void scheduleActivity(FlowNode flowNode) {
        this.addBehaviour(ActivityFactory.create(flowNode, this));
    }
}
