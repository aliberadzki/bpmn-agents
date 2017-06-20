package pl.aliberadzki.bpmnagents.actions;

import pl.aliberadzki.bpmnagents.actions.booktrading.Buy;
import pl.aliberadzki.bpmnagents.actions.booktrading.CheckPrice;
import pl.aliberadzki.bpmnagents.actions.booktrading.Reject;
import pl.aliberadzki.bpmnagents.actions.booktrading.Wait;
import pl.aliberadzki.bpmnagents.activities.TaskActivity;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class ActionFactory {

    public static Action makeAction(TaskActivity taskActivity, String name)
    {
        if(name.equals("check price") || name.equals("sprawdz kwote"))
            return new CheckPrice(taskActivity);

        if(name.equals("display reject") || name.equals("zrezygnuj"))
            return new Reject(taskActivity);

        if(name.equals("display buy") || name.equals("kup"))
            return new Buy(taskActivity);

        if(name.contains("wait"))
            return new Wait(taskActivity);
        return new Nothing();
    }
}
