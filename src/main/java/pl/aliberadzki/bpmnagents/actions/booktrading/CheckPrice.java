package pl.aliberadzki.bpmnagents.actions.booktrading;

import pl.aliberadzki.bpmnagents.actions.Action;
import pl.aliberadzki.bpmnagents.activities.TaskActivity;

/**
 * Created by aliberadzki on 13.05.17.
 */
public class CheckPrice implements Action {
    private TaskActivity taskActivity;

    public CheckPrice(TaskActivity taskActivity) {
        this.taskActivity = taskActivity;
    }

    @Override
    public boolean execute() {
        String title = (String) taskActivity.getInput("bookName");
        Integer value = getBookPrice(title);
        taskActivity.setOutput("bookPrice", value);
        return true;
    }

    private Integer getBookPrice(String title) {
        if(title.equals("Harry Potter") ) return 150;
        if(title.equals("Hunger Games") ) return 110;
        if(title.equals("50 Shades of Grey") ) return 10;
        return 50;
    }
}
