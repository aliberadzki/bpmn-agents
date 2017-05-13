package pl.aliberadzki.bpmnagents.knowledge;


/**
 * Created by aliberadzki on 09.05.17.
 */
public class Belief {
    private String name;
    private String type;
    private Object value;
    private Boolean isList;

    public Belief(String name, String type, Object value, Boolean isList) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.isList = isList;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
