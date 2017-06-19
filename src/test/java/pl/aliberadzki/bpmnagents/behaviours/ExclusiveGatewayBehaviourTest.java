package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.junit.Before;
import org.junit.Test;
import pl.aliberadzki.bpmnagents.BpmnAgent;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by aliberadzki on 10.05.2017.
 */
public class ExclusiveGatewayBehaviourTest {
    private BpmnAgent myAgent;
    private ExclusiveGateway gateway;

    @Before
    public void setUp() throws Exception {
        this.myAgent = mock(BpmnAgent.class);
        this.gateway = mock(ExclusiveGateway.class);
    }

    @Test
    public void testItFiresOnlyOneOutgoingFlow() throws Exception {
        when(myAgent.anyIncomingRouteActive(any())).thenReturn(true);
        ExclusiveGatewayBehaviour behaviour = new ExclusiveGatewayBehaviour(this.myAgent, this.gateway);
        assertTrue(behaviour.execute());
        assertTrue(behaviour.isReady());
    }
}