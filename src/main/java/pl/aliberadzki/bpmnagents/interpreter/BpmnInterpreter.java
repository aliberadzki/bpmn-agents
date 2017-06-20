package pl.aliberadzki.bpmnagents.interpreter;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;
import pl.aliberadzki.bpmnagents.activities.ActivityFactory;
import pl.aliberadzki.bpmnagents.behaviours.BpmnBehaviour;

import java.io.InputStream;
import java.util.*;

/**
 * Created by aliberadzki on 09.05.2017.
 */
public class BpmnInterpreter {

    private Process bpmnProcess = null;

    private Collection<BpmnBehaviour> activityBehaviours = new ArrayList<>();
    private Collection<SequenceFlow> activeFlows = new ArrayList<>();
    private Collection<SequenceFlow> finishedFlows = new ArrayList<>();

    private BpmnAgent myAgent;
    private BpmnEventListenerManager eventManager;

    private String bpdName;
    private String participantId;
    private Collection<MessageFlow> messageFlows;

    public BpmnInterpreter(BpmnAgent bpmnAgent, String bpdName, String participantId, List paramList)
    {
        this.myAgent = bpmnAgent;
        this.bpdName = bpdName;
        this.participantId = participantId;
        this.eventManager = new BpmnEventListenerManager(bpmnAgent, this.loadProcessDefinition());
        this.initKnowledge(paramList);
    }

    public void markFlowAsActive(Collection<SequenceFlow> sequenceFlows)
    {
        activeFlows.addAll(sequenceFlows);
    }

    public boolean isFlowActive(SequenceFlow sequenceFlow)
    {
        return activeFlows.contains(sequenceFlow);
    }

    public void setFlowFinished(SequenceFlow flow)
    {
        this.activeFlows.removeIf(flow1 -> flow1.equals(flow));
        this.finishedFlows.add(flow);
    }

    public void scheduleActivity(FlowNode flowNode)
    {
        Activity activity = ActivityFactory.create(flowNode, myAgent);
        if(activity == null) return;
        BpmnBehaviour behaviour = new BpmnBehaviour(myAgent, flowNode, activity);
        myAgent.addBehaviour(behaviour);
        this.activityBehaviours.add(behaviour);
    }

    public void cancelBehaviour(String activityId)
    {
        BpmnBehaviour behaviour = this.activityBehaviours.stream()
                .filter(bpmnBehaviour -> bpmnBehaviour.getId().equals(activityId))
                .findFirst()
                .orElse(null);

        if(behaviour == null ) return;
        myAgent.log("Cancelling behaviour : " + activityId);
        eventManager.cancelEventListenersOn(activityId);
        myAgent.removeBehaviour(behaviour);
        this.activityBehaviours.remove(behaviour);
    }

    public void removeProcessStarters() {
        eventManager.cleanStartEventBehaviours();
    }


    public void scheduleBoundaryEventsFor(Task task)
    {
        bpmnProcess.getModelInstance()
                .getModelElementsByType(BoundaryEvent.class)
                .stream()
                .filter(boundaryEvent -> isAttachedTo(task, boundaryEvent))
                .forEach(eventManager::addEventListener);
    }

    public void cancelBoundaryEventsFor(Task task)
    {
        bpmnProcess.getModelInstance()
                .getModelElementsByType(BoundaryEvent.class)
                .stream()
                .filter(boundaryEvent -> isAttachedTo(task, boundaryEvent))
                .forEach(boundaryEvent -> eventManager.cancelEventListener(boundaryEvent.getId()));
    }

    public boolean isAnyIncomingRouteActive(FlowNode flowNode)
    {
        return flowNode.getIncoming().stream()
                .anyMatch(this::isFlowActive);
    }

    public boolean areAllIncomingRoutesActive(FlowNode flowNode)
    {
        return flowNode.getIncoming().stream()
                .allMatch(this::isFlowActive);
    }

    public void activateFlows(Collection<SequenceFlow> sequenceFlows)
    {
        markFlowAsActive(sequenceFlows);
        sequenceFlows.forEach(sequenceFlow -> scheduleActivity(sequenceFlow.getTarget()));
    }

    public void deactivateFlows(Collection<SequenceFlow> sequenceFlows)
    {
        sequenceFlows.forEach(this::setFlowFinished);
    }

    public void cancelActivityWithAttachee(BoundaryEvent event)
    {
        cancelBehaviour(event.getAttachedTo().getId());
    }

    private void initKnowledge(List paramList)
    {
        IoSpecification ioSpecification = bpmnProcess.getIoSpecification();
        if(ioSpecification == null) return;
        Collection<DataInput> dataInputs = ioSpecification.getDataInputs();
        Iterator<DataInput> iterator = dataInputs.iterator();
        for(int i=0; i<paramList.size() && iterator.hasNext(); i++) {
            String factName = iterator.next().getName();
            Object factValue = paramList.get(i);
            myAgent.recognizeFact(factName, factValue);
        }
    }

    private boolean isAttachedTo(Task task, BoundaryEvent boundaryEvent)
    {
        return boundaryEvent.getAttachedTo().getId().equals(task.getId());
    }

    private Process loadProcessDefinition()
    {
        if(bpmnProcess != null) return bpmnProcess;
        InputStream bpdStream = getClass()
                .getClassLoader()
                .getResourceAsStream(bpdName + ".bpmn");
        BpmnModelInstance modelInstance = bpdStream!=null
                ? Bpmn.readModelFromStream(bpdStream)
                : Bpmn.createEmptyModel();
        String processRef = modelInstance
                .getModelElementById(participantId)
                .getAttributeValue("processRef");

        bpmnProcess = modelInstance.getModelElementById(processRef);
        messageFlows = modelInstance.getModelElementsByType(MessageFlow.class);
        return bpmnProcess;
    }

    public String getParticipantIdForSenderTask(Task task) {
        Optional<MessageFlow> messageFlow = getMessageFlowForTask(task);
        if(!messageFlow.isPresent()) return null;
        String pid = getProcessIdForMessageFlow(messageFlow.get());
        return getParticipantIdForProcessId(pid);
//        task(sendTaskId).outgoingMessageFlow().target().process().participant().getId();
    }

    public Optional<MessageFlow> getMessageFlowForTask(Task task)
    {
        //FIXME ugly
        if(task instanceof ReceiveTask) {
            return messageFlows.stream()
                    .filter(mf -> mf.getTarget().getId().equals(task.getId()))
                    .findFirst();
        }
        return messageFlows.stream()
                .filter(mf -> mf.getSource().getId().equals(task.getId()))
                .findFirst();
    }

    private String getProcessIdForMessageFlow(MessageFlow messageFlow) {
        return ((Process)messageFlow.getTarget().getParentElement()).getId();
    }

    private String getParticipantIdForProcessId(String pid) {
        Optional<Participant> participant = this.bpmnProcess
                .getModelInstance()
                .getModelElementsByType(Participant.class)
                .stream()
                .filter(p -> p.getProcess().getId().equals(pid))
                .findFirst();
        return participant.map(BaseElement::getId).orElse(null);
    }

    public void blockBpmnBehaviour(Activity activity, long period)
    {
        BpmnBehaviour bpmnBehaviour = this.activityBehaviours.stream()
                .filter(b -> b.has(activity))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        bpmnBehaviour.block(period);
    }
}
