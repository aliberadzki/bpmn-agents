package pl.aliberadzki.bpmnagents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import pl.aliberadzki.bpmnagents.BpmnAgent;

/**
 * Created by aliberadzki on 04.05.17.
 */
public class TimerStartBehaviour extends TickerBehaviour {

    private boolean completed = false;

    public TimerStartBehaviour(BpmnAgent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        System.out.println("TIMER START BEHAVIOUR FINISHED");
        this.stop();
        ((BpmnAgent)myAgent).cleanStartEventBehaviours();
    }
}
