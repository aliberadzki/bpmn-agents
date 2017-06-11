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
    public boolean execute() {
        String title = (String) taskBehaviour.getInput("bookName");
        Integer value = getBookPrice(title);
        taskBehaviour.setOutput("bookPrice", value);
        return true;
    }

    private Integer getBookPrice(String title) {
        if(title.equals("Harry Potter") ) return 150;
        if(title.equals("Hunger Games") ) return 110;
        if(title.equals("50 Shades of Grey") ) return 10;
        return 50;
    }
}
