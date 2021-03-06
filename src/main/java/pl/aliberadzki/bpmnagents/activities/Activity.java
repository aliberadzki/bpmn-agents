package pl.aliberadzki.bpmnagents.activities;

/**
 * Created by aliberadzki on 19.06.17.
 */
public interface Activity {
    boolean isReady();

    boolean execute();

    void afterFinish();

    void block(long period);

    void onStart();

    void onEnd();
}
