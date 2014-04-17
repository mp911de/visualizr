package biz.paluch.visualizr.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:15
 */
public class Datasource implements Serializable {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    public Datasource() {
    }

    public Datasource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
