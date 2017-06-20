package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.activities.TaskActivity;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class Buy implements Action {
    private TaskActivity taskActivity;

    public Buy(TaskActivity taskActivity) {
        this.taskActivity = taskActivity;
    }

    @Override
    public boolean execute() {
        String title = (String) taskActivity.getInput("bookName");
        Integer price = Integer.valueOf((String) taskActivity.getInput("bookPrice"));
        taskActivity.log("I am going to buy book titled " + title + ". It costs only " + price);
        return true;
    }
}
