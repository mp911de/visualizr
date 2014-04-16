package biz.paluch.visualizr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 13.04.14 11:16
 */
public class ChartDescriptor {
    private String id;
    private String title;
    private String xAxisTitle;
    private String yAxisTitle;
    private String titleField;
    private String categoryField;
    private int precision;
    private List<ChartGraphDescriptor> graphs = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxAxisTitle() {
        return xAxisTitle;
    }

    public void setxAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public List<ChartGraphDescriptor> getGraphs() {
        return graphs;
    }

    public void setGraphs(List<ChartGraphDescriptor> graphs) {
        this.graphs = graphs;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
