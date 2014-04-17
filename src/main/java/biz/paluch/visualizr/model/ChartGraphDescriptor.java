package biz.paluch.visualizr.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:18
 */
public class ChartGraphDescriptor implements Serializable {
    private String id;
    private String name;

    public ChartGraphDescriptor() {
    }

    public ChartGraphDescriptor(String id, String name) {
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
