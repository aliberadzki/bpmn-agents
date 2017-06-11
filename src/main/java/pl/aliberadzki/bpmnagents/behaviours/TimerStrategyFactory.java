package pl.aliberadzki.bpmnagents.behaviours;

import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;

import java.time.Duration;

/**
 * Created by aliberadzki on 11.06.17.
 */
public class TimerStrategyFactory {
    public static TimerStrategy create(TimerEventDefinition eventDefinition)
    {
        String[] intervalStr = eventDefinition.getTextContent().split("/");

        return new TimerStrategy() {
            @Override
            public int repeatCount() {
                return parseRepeats(intervalStr);
            }

            @Override
            public long getPeriod() {
                return parsePeriod(intervalStr);
            }
        };
    }

    private static int parseRepeats(String[] intervalStr)
    {
        if(intervalStr.length <= 1) return -1;
        if("R".equals(intervalStr[0])) return 99;
        return Integer.valueOf(intervalStr[0].substring(1));
    }

    private static long parsePeriod(String[] intervalStr)
    {
        if(intervalStr.length == 1) return Duration.parse(intervalStr[0]).getSeconds()*1000;
        return Duration.parse(intervalStr[1]).getSeconds()*1000;
    }
}
