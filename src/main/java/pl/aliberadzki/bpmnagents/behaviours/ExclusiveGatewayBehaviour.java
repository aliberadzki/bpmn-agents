package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Created by aliberadzki on 08.05.17.
 */
public class ExclusiveGatewayBehaviour extends BpmnBehaviour {
    private BpmnAgent bpmnAgent;
    private final ExclusiveGateway gateway;

    public ExclusiveGatewayBehaviour(BpmnAgent bpmnAgent, ExclusiveGateway gateway) {
        super(bpmnAgent, gateway);
        this.bpmnAgent = bpmnAgent;
        this.gateway = gateway;
    }

    @Override
    protected boolean canRun() {
        return this.anyIncomingRouteActive();
    }

    @Override
    protected boolean execute() {
        bpmnAgent.log("Gateway executing: " + gateway.getName() + " (" + gateway.getId() + ')');
        return true;
    }

    @Override
    protected Collection<SequenceFlow> getOutgoing() {
        SequenceFlow flow = gateway.getOutgoing().stream()
                .filter(this::isConditionTrue)
                .findFirst()
                .get();
        //TODO: or else return default flow
        return Collections.singletonList(flow);
    }

    private boolean isConditionTrue(SequenceFlow sequenceFlow) {
        String expression = sequenceFlow.getConditionExpression().getTextContent();
        bpmnAgent.log("Checking if true: " + expression);
        //TODO: it should somehow use the agent knowledge of its state or beliefs
        return expression.contains(">=100");
    }
}
