package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.knowledge.Expression;

import java.util.Collection;
import java.util.Collections;

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
                .orElse(gateway.getDefault());
        return Collections.singletonList(flow);
    }

    private boolean isConditionTrue(SequenceFlow sequenceFlow) {
        ConditionExpression expression = sequenceFlow.getConditionExpression();
        if(expression == null) return false;

        String exprContent = expression.getTextContent();
        Boolean result = bpmnAgent.evaluateExpression(new Expression(exprContent));
        bpmnAgent.log("Checking if true: " + exprContent + " " + result);
        return result;
    }
}
