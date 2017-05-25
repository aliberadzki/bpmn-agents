package pl.aliberadzki.bpmnagents.actions;

import pl.aliberadzki.bpmnagents.actions.booktrading.Buy;
import pl.aliberadzki.bpmnagents.actions.booktrading.CheckPrice;
import pl.aliberadzki.bpmnagents.actions.booktrading.Reject;
import pl.aliberadzki.bpmnagents.actions.booktrading.Wait;
import pl.aliberadzki.bpmnagents.behaviours.TaskBehaviour;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class ActionFactory {

    public static Action makeAction(TaskBehaviour taskBehaviour, String name)
    {
        if(name.equals("check price") || name.equals("sprawdz kwote"))
            return new CheckPrice(taskBehaviour);

        if(name.equals("display reject") || name.equals("zrezygnuj"))
            return new Reject(taskBehaviour);

        if(name.equals("display buy") || name.equals("kup"))
            return new Buy(taskBehaviour);

        if(name.equals("wait"))
            return new Wait(taskBehaviour);
        return new Nothing();
    }
}
