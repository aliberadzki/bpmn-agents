package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class TimerStartBehaviour extends TickerBehaviour implements StartBehaviour{

    private StartEvent event;

    public TimerStartBehaviour(BpmnAgent a, StartEvent event) {
        super(a, 20000);
        this.event = event;
    }

    @Override
    protected void onTick() {
        System.out.println("TIMER START BEHAVIOUR FINISHED");
        this.stop();
        ((BpmnAgent)myAgent).cleanStartEventBehaviours();
        //TODO activate outgoing route
//        event.getOutgoing().iterator().next()
    }
}
