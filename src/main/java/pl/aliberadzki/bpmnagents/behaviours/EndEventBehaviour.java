package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.behaviours.Behaviour;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 05.05.2017.
 */
public class EndEventBehaviour extends Behaviour {
    private EndEvent endEvent = null;

    public EndEventBehaviour(BpmnAgent agent, EndEvent endEvent) {
        super(agent);
        this.endEvent = endEvent;
    }

    @Override
    public void action() {
        if(this.endEvent == null) {
            System.out.println("EXECUTION of unknown endEvent ");
            return;
        }
        System.out.println("EXECUTION of endEvent " + this.endEvent.getId());
        //TODO if terminate then terminate current process.
    }

    @Override
    public boolean done() {
        return true;
    }
}
