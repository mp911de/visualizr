package biz.paluch.visualizr.metrics;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 03.07.14 13:18
 */
public class MetricRef {
    private String metric;
    private String value;
    private String caption;
    private double multiplier = 1;

    public MetricRef() {
    }

    public MetricRef(String metric, String value) {
        this.metric = metric;
        this.value = value;
    }

    public MetricRef(String metric, String value, String caption) {
        this.metric = metric;
        this.value = value;
        this.caption = caption;
    }

    public MetricRef(String metric, String value, String caption, double multiplier) {
        this.metric = metric;
        this.value = value;
        this.caption = caption;
        this.multiplier = multiplier;
    }

    public MetricRef(String metric, String value, double multiplier) {
        this.metric = metric;
        this.value = value;
        this.multiplier = multiplier;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
