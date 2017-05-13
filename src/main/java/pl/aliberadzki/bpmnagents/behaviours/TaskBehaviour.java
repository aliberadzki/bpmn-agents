package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.DataOutput;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.actions.ActionFactory;
import pl.aliberadzki.bpmnagents.knowledge.Belief;

import java.util.*;


/**
 * Created by aliberadzki on 05.05.2017.
 */
public class TaskBehaviour extends BpmnBehaviour {
    private final Action action;
    private BpmnAgent bpmnAgent;
    private Task task;

    private Map<String, Belief> inputs = new HashMap<>();
    private Map<String, Belief> outputs = new HashMap<>();

    public TaskBehaviour(BpmnAgent bpmnAgent, Task task) {
        super(bpmnAgent, task);
        this.bpmnAgent = bpmnAgent;
        this.task = task;
        this.action = ActionFactory.makeAction(this, task.getName());
        this.initIO();
    }

    private void initIO() {
        task.getIoSpecification()
                .getDataInputs()
                .forEach(this::addInput);
        task.getIoSpecification()
                .getDataOutputs()
                .forEach(this::addOutput);
    }

    private void addOutput(DataOutput dataOutput) {
        bpmnAgent.log("[OUT] " + dataOutput.getName() + " : " + dataOutput.getAttributeValue("itemSubjectRef"));
        outputs.put(dataOutput.getName(), new Belief(
                dataOutput.getName(),
                dataOutput.getAttributeValue("itemSubjectRef"),
                null,
                dataOutput.isCollection()));
    }

    private void addInput(DataInput dataInput) {
        bpmnAgent.log("[IN]  " + dataInput.getName() + " : " + dataInput.getAttributeValue("itemSubjectRef"));
        Object value = bpmnAgent.getProcessValue(dataInput.getName());
        inputs.put(dataInput.getName(), new Belief(
                dataInput.getName(),
                dataInput.getAttributeValue("itemSubjectRef"),
                value,
                dataInput.isCollection()));
    }

    @Override
    public void onStart()
    {
        bpmnAgent.addBoundaryEventsFor(task);
    }

    @Override
    public int onEnd() {
        bpmnAgent.clearBoundaryEventsFor(task);
        this.outputs.forEach((key, value) -> bpmnAgent.recognizeFact(key, ((Belief)value).getValue()));
        return super.onEnd();
    }

    @Override
    protected boolean canRun() {
        return anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        bpmnAgent.log("EXECUTION OF TASK " + task.getName() + " (" + task.getId() + ")");
        action.execute();
//        setOutput("bookPrice", 150);
//        bpmnAgent.recognizeFact("bookPrice", 150);
        return true;
    }

    public void setOutput(String name, Object value) {
        Belief out = outputs.get(name);
        //TODO: exception?
        if(out == null) return;
        out.setValue(value);
    }

    public Object getInput(String name)
    {
        return inputs.get(name).getValue();
    }

    public void log(String text)
    {
        bpmnAgent.log(" [" + task.getName() + "] " + text);
    }
}
