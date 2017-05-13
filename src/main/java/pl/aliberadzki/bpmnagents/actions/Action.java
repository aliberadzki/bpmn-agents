package pl.aliberadzki.bpmnagents.actions;

/**
 * Created by aliberadzki on 13.05.17.
 */
public interface Action {
    /**
     * Attempts to perform operation needed to complete task
     * @return is operation completed. If false agent should retry.
     */
    boolean execute();
}
