package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.activities.TaskActivity;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class Reject implements Action {
    private TaskActivity taskActivity;

    public Reject(TaskActivity taskActivity) {
        this.taskActivity = taskActivity;
    }

    @Override
    public boolean execute() {
        String title = (String) taskActivity.getInput("bookName");
        Integer price = Integer.valueOf((String) taskActivity.getInput("bookPrice"));
        taskActivity.log("I am not going to buy book titled " + title + ". It costs " + price);
        return true;
    }
}
