package biz.paluch.visualizr.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 03.07.14 13:16
 */
public class ComposedChart {
    private String id;
    private String name;
    private String units;
    private List<MetricRef> metrics = new ArrayList<>();

    public ComposedChart() {
    }

    public ComposedChart(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ComposedChart(String id, String name, String units) {
        this.id = id;
        this.name = name;
        this.units = units;
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

    public List<MetricRef> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricRef> metrics) {
        this.metrics = metrics;
    }

    public void addMetricRef(MetricRef metricRef) {
        this.metrics.add(metricRef);

    }

    public void addMetricRef(String metric, String value) {
        addMetricRef(new MetricRef(metric, value));
    }

    public void addMetricRef(String metric, String value, double multiplier) {
        addMetricRef(new MetricRef(metric, value, multiplier));
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
