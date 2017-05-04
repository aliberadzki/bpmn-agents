package pl.aliberadzki.bpmnagents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.behaviours.MsgStartBehaviour;
import pl.aliberadzki.bpmnagents.behaviours.StartEventFactory;
import pl.aliberadzki.bpmnagents.behaviours.TimerStartBehaviour;

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

    @Override
    protected void setup()
    {
        this.bpdName = (String) getArguments()[0];
        this.participantId = (String) getArguments()[1];


        this.initStartEventListeners();
    }

    private void initStartEventListeners()
    {
        Process process = this.getProcess();
        for (StartEvent startEvent : getStartEvent(process)) {
            addStartEventListener(startEvent);
        }
    }

    private void addStartEventListener(StartEvent startEvent) {
        Behaviour behaviour = StartEventFactory.create(startEvent, this);
        if(behaviour == null) return;
        this.startBehaviours.add(behaviour);
        this.addBehaviour(behaviour);
    }

    public void cleanStartEventBehaviours()
    {
        this.startBehaviours.forEach(this::removeBehaviour);
    }

    private Process getProcess()
    {
        InputStream bpdStream = getClass().getClassLoader().getResourceAsStream(bpdName + ".bpmn");
        BpmnModelInstance modelInstance = bpdStream!=null ? Bpmn.readModelFromStream(bpdStream) : Bpmn.createEmptyModel();
        String processRef = modelInstance.getModelElementById(participantId).getAttributeValue("processRef");

        return modelInstance.getModelElementById(processRef);
    }

    private Collection<StartEvent> getStartEvent(Process process)
    {
        return process.getFlowElements().stream()
                .filter(StartEvent.class::isInstance)
                .map(StartEvent.class::cast)
                .collect(Collectors.toList());
    }
}
