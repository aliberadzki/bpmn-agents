package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.DataOutput;
import org.camunda.bpm.model.bpmn.instance.IoSpecification;
import org.camunda.bpm.model.bpmn.instance.Task;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.actions.ActionFactory;
import pl.aliberadzki.bpmnagents.knowledge.Belief;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by aliberadzki on 05.05.2017.
 */
//TODO RENAME
public class TaskBehaviour implements Activity {
    private final Action action;
    private BpmnAgent bpmnAgent;
    private Task task;

    private Map<String, Belief> inputs = new HashMap<>();
    private Map<String, Belief> outputs = new HashMap<>();

    public TaskBehaviour(BpmnAgent bpmnAgent, Task task)
    {
        this.bpmnAgent = bpmnAgent;
        this.task = task;
        this.action = ActionFactory.makeAction(this, task.getName());
        this.initIO();
    }

    //TODO that doesnt get fired
    public void onStart()
    {
        bpmnAgent.addBoundaryEventsFor(task);
    }

    public void onEnd()
    {
        bpmnAgent.clearBoundaryEventsFor(task);
        passTaskOutputsToAgent();
    }

    @Override
    public boolean isReady() {
        return bpmnAgent.anyIncomingRouteActive(task);
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("EXECUTION OF TASK " + task.getName() + " (" + task.getId() + ")");
        return action.execute();
    }

    @Override
    public void afterFinish() {

    }

    @Override
    public void block(long period)
    {
        bpmnAgent.blockBehaviour(this, period);
    }

    public void setOutput(String name, Object value)
    {
        Belief out = outputs.get(name);
        if(out == null) {
            throw new RuntimeException("No output named " + name + " defined!");
        }
        out.setValue(value);
        bpmnAgent.recognizeFact(name,value);
    }

    public Object getInput(String name)
    {
        return inputs.get(name).getValue();
    }

    public void log(String text)
    {
        bpmnAgent.log(" [" + task.getName() + "] " + text);
    }

    private void initIO()
    {
        IoSpecification ioSpecification = task.getIoSpecification();
        if(ioSpecification == null) return;
        ioSpecification
                .getDataInputs()
                .forEach(this::addInput);
        ioSpecification
                .getDataOutputs()
                .forEach(this::addOutput);
    }

    private void addInput(DataInput dataInput)
    {
        bpmnAgent.log("[IN]  " + dataInput.getName() + " : " + dataInput.getAttributeValue("itemSubjectRef"));
        Object value = bpmnAgent.getProcessValue(dataInput.getName());
        inputs.put(dataInput.getName(), new Belief(
                dataInput.getName(),
                dataInput.getAttributeValue("itemSubjectRef"),
                value,
                dataInput.isCollection()));
    }

    private void addOutput(DataOutput dataOutput)
    {
        bpmnAgent.log("[OUT] " + dataOutput.getName() + " : " + dataOutput.getAttributeValue("itemSubjectRef"));
        outputs.put(dataOutput.getName(), new Belief(
                dataOutput.getName(),
                dataOutput.getAttributeValue("itemSubjectRef"),
                null,
                dataOutput.isCollection()));
    }

    private void passTaskOutputsToAgent()
    {
        this.outputs.forEach((key, value) -> bpmnAgent.recognizeFact(key, value.getValue()));
    }

    protected Map<String, Belief> getInputs()
    {
        return this.inputs;
    }
}
