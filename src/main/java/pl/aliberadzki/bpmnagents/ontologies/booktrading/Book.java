package pl.aliberadzki.bpmnagents.ontologies.booktrading;

import jade.content.Concept;

import java.util.List;

/**
 * Created by aliberadzki on 08.05.17.
 */
public class Book implements Concept{
    private String title;
    private List authors;
    private String editor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getAuthors() {
        return authors;
    }

    public void setAuthors(List authors) {
        this.authors = authors;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
