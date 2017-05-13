package pl.aliberadzki.bpmnagents.actions;

import pl.aliberadzki.bpmnagents.actions.booktrading.Buy;
import pl.aliberadzki.bpmnagents.actions.booktrading.CheckPrice;
import pl.aliberadzki.bpmnagents.actions.booktrading.Reject;
import pl.aliberadzki.bpmnagents.behaviours.TaskBehaviour;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class ActionFactory {

    public static Action makeAction(TaskBehaviour taskBehaviour, String name)
    {
        if(name.equals("check price"))
            return new CheckPrice(taskBehaviour);

        if(name.equals("display reject"))
            return new Reject(taskBehaviour);

        if(name.equals("display buy"))
            return new Buy(taskBehaviour);
        return null;
    }
}
