package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.behaviours.TaskBehaviour;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class CheckPrice implements Action {
    private TaskBehaviour taskBehaviour;

    public CheckPrice(TaskBehaviour taskBehaviour) {
        this.taskBehaviour = taskBehaviour;
    }

    @Override
    public void execute() {
        String title = (String) taskBehaviour.getInput("bookName");
        Integer value = title.equals("Harry Potter") ? 150 : 50;
        taskBehaviour.setOutput("bookPrice", value);

    }
}
