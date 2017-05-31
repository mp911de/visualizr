package biz.paluch.visualizr.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @author <a href="mailto:stephan.frigger@kaufland.de">Stephan Frigger</a>
 * @since 01.07.14 12:30
 */
public class MetricItem {
    private String name;
    private String description;
    private String units;

    public MetricItem(String name, String units) {
        this.name = name;
        this.units = units;
    }

    public MetricItem(String name, String description, String units) {
        this.name = name;
        this.description = description;
        this.units = units;
    }

    public MetricItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUnits() {
        return units;
    }

    public String getDescription() {
        if (description != null) {
            return description;
        }
        return getName();
    }

    public static class Builder {

        private List<MetricItem> items = new ArrayList<>();

        private Builder() {

        }

        public static Builder create() {
            return new Builder();
        }

        public Builder count(String name) {
            items.add(new MetricItem(name));
            return this;
        }

        public Builder calls(String name) {
            items.add(new MetricItem(name, "calls"));
            return this;
        }

        public Builder calls(String name, TimeUnit timeUnit) {
            items.add(new MetricItem(name, "calls/" + getUnit(timeUnit)));
            return this;
        }

        public Builder calls(String name, int timeUnitCount, TimeUnit timeUnit) {
            items.add(new MetricItem(name, "calls/" + timeUnitCount + " " + getUnit(timeUnit)));
            return this;
        }

        public Builder duration(String name, TimeUnit timeUnit) {
            items.add(new MetricItem(name, getUnit(timeUnit)));
            return this;
        }

        public Builder events(String name) {
            items.add(new MetricItem(name));
            return this;
        }

        public Builder events(String name, TimeUnit timeUnit) {
            items.add(new MetricItem(name, "events/" + getUnit(timeUnit)));
            return this;
        }

        public Builder events(String name, int timeUnitCount, TimeUnit timeUnit) {
            items.add(new MetricItem(name, "events/" + timeUnitCount + " " + getUnit(timeUnit)));
            return this;
        }

        public Builder events(String name, String description) {
            items.add(new MetricItem(name, description));
            return this;
        }

        private String getUnit(TimeUnit timeUnit) {
            final String s = timeUnit.toString().toLowerCase(Locale.US);
            return s.substring(0, s.length() - 1);
        }

        public List<MetricItem> build() {
            return new ArrayList<>(items);
        }
    }
}
