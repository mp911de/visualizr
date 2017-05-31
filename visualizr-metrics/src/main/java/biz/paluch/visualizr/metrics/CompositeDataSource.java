package biz.paluch.visualizr.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 03.07.14 07:57
 */
public class CompositeDataSource {
    private String id;
    private String name;
    private List<ComposedChart> charts = new ArrayList<>();

    public CompositeDataSource() {
    }

    public CompositeDataSource(String id, String name) {
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

    public void addChart(ComposedChart chart) {
        this.charts.add(chart);
    }

    public List<ComposedChart> getCharts() {
        return charts;
    }

    public void setCharts(List<ComposedChart> charts) {
        this.charts = charts;
    }
}
