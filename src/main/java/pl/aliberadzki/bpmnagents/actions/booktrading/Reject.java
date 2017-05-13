package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.behaviours.TaskBehaviour;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class Reject implements Action {
    private TaskBehaviour taskBehaviour;

    public Reject(TaskBehaviour taskBehaviour) {
        this.taskBehaviour = taskBehaviour;
    }

    @Override
    public boolean execute() {
        String title = (String) taskBehaviour.getInput("bookName");
        Integer price = (Integer) taskBehaviour.getInput("bookPrice");
        taskBehaviour.log("I am not going to buy book titled " + title + ". It costs " + price);
        return true;
    }
}
