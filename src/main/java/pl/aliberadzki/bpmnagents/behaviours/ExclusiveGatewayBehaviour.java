package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import pl.aliberadzki.bpmnagents.BpmnAgent;
import pl.aliberadzki.bpmnagents.activities.Activity;
import pl.aliberadzki.bpmnagents.knowledge.Expression;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by aliberadzki on 08.05.17.
 */
public class ExclusiveGatewayBehaviour implements Activity {
    private BpmnAgent bpmnAgent;
    private final ExclusiveGateway gateway;

    public ExclusiveGatewayBehaviour(BpmnAgent bpmnAgent, ExclusiveGateway gateway)
    {
        this.bpmnAgent = bpmnAgent;
        this.gateway = gateway;
    }

    @Override
    public boolean isReady() {
        return bpmnAgent.anyIncomingRouteActive(gateway);
    }

    @Override
    public boolean execute()
    {
        bpmnAgent.log("Gateway executing: " + gateway.getName() + " (" + gateway.getId() + ')');
        return true;
    }

    @Override
    public void afterFinish() {

    }

    @Override
    public void block(long period) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    protected Collection<SequenceFlow> getOutgoing()
    {
        SequenceFlow flow = gateway.getOutgoing().stream()
                .filter(this::isFlowConditionTrue)
                .findFirst()
                .orElse(gateway.getDefault());
        return Collections.singletonList(flow);
    }

    private boolean isFlowConditionTrue(SequenceFlow sequenceFlow)
    {
        ConditionExpression expression = sequenceFlow.getConditionExpression();
        if(expression == null) return false;

        String exprContent = expression.getTextContent();
        Boolean result = bpmnAgent.evaluateExpression(new Expression(exprContent));
        bpmnAgent.log("Checking if true: " + exprContent + " " + result);
        return result;
    }
}
