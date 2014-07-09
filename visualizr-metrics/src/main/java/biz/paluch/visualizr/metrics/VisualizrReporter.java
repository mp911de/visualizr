package biz.paluch.visualizr.metrics;

import static com.codahale.metrics.MetricRegistry.name;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

/**
 * Reporter to submit metrics to visualizr.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 01.07.14 08:20
 */
public class VisualizrReporter extends ScheduledReporter {

    /**
     * Returns a new {@link Builder} for {@link VisualizrReporter}.
     * 
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link VisualizrReporter}
     */
    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    /**
     * A builder for {@link VisualizrReporter} instances. Defaults to using a {@code tmax} of {@code 60}, a {@code dmax} of
     * {@code 0}, converting rates to events/second, converting durations to milliseconds, and not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry registry;
        private String prefix;
        private Clock clock;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.clock = Clock.defaultClock();
            this.rateUnit = TimeUnit.MINUTES;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
        }

        /**
         * Prefix all metric names with the given string.
         * 
         * @param prefix the prefix for all metric names
         * @return {@code this}
         */
        public Builder prefixedWith(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Use the supplied clock.
         * 
         * @param clock
         * @return {@code this}
         */
        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         * 
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         * 
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         * 
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Builds a {@link VisualizrReporter} with the given properties, pushing metrics to a visualizr snapshots store.
         * 
         * @param snapshots the client to use for announcing metrics
         * @return a {@link VisualizrReporter}
         */
        public VisualizrReporter build(Snapshots snapshots) {
            return new VisualizrReporter(registry, snapshots, prefix, clock, rateUnit, durationUnit, filter);
        }
    }

    private final Snapshots snapshots;
    private final String prefix;
    private final Clock clock;

    private final TimeUnit durationUnit;
    private final TimeUnit rateUnit;

    private VisualizrReporter(MetricRegistry registry, Snapshots snapshots, String prefix, Clock clock, TimeUnit rateUnit,
            TimeUnit durationUnit, MetricFilter filter) {
        super(registry, "visualizr-reporter", filter, rateUnit, durationUnit);
        this.snapshots = snapshots;
        this.prefix = prefix;
        this.clock = clock;
        this.rateUnit = rateUnit;
        this.durationUnit = durationUnit;
        snapshots.setDurationUnit(durationUnit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
            SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {

        try {
            for (Map.Entry<String, Gauge> entry : gauges.entrySet()) {
                reportGauge(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Counter> entry : counters.entrySet()) {
                reportCounter(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Histogram> entry : histograms.entrySet()) {
                reportHistogram(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Meter> entry : meters.entrySet()) {
                reportMeter(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Timer> entry : timers.entrySet()) {
                reportTimer(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Report a timer using fields max/mean/min/stddev,p50/p75/p95/p98/p99/p999/calls/m1_rate/m5_rate/m15_rate/mean_rate
     * 
     * @param name
     * @param timer
     */
    private void reportTimer(String name, Timer timer) {
        final Snapshot snapshot = timer.getSnapshot();
        String prefixedName = prefix(name);
        if (!snapshots.hasDescriptor(prefixedName)) {

            MetricItem.Builder builder = MetricItem.Builder.create();
            builder.duration("max", durationUnit);
            builder.duration("mean", durationUnit);
            builder.duration("min", durationUnit);
            builder.duration("stddev", durationUnit);

            builder.duration("p50", durationUnit);
            builder.duration("p75", durationUnit);
            builder.duration("p95", durationUnit);
            builder.duration("p98", durationUnit);
            builder.duration("p99", durationUnit);
            builder.duration("p95", durationUnit);
            builder.duration("p999", durationUnit);

            builder.calls("calls");

            builder.calls("m1_rate", 1, rateUnit);
            builder.calls("m5_rate", 1, rateUnit);
            builder.calls("m15_rate", 1, rateUnit);
            builder.calls("mean_rate", rateUnit);

            snapshots.setDescriptor(prefixedName, builder.build());

        }

        Map<String, Number> values = new HashMap<String, Number>();

        values.put("max", convertDuration(snapshot.getMax()));
        values.put("mean", convertDuration(snapshot.getMean()));
        values.put("min", convertDuration(snapshot.getMin()));
        values.put("stddev", convertDuration(snapshot.getStdDev()));

        values.put("p50", convertDuration(snapshot.getMedian()));
        values.put("p75", convertDuration(snapshot.get75thPercentile()));
        values.put("p95", convertDuration(snapshot.get95thPercentile()));
        values.put("p98", convertDuration(snapshot.get98thPercentile()));
        values.put("p99", convertDuration(snapshot.get99thPercentile()));
        values.put("p999", convertDuration(snapshot.get999thPercentile()));

        addMetered(timer, values, "calls");

        snapshots.addSnapshot(prefixedName, getTimestamp(), values);

    }

    /**
     * Report a meter using fields events/m1_rate/m5_rate/m15_rate/mean_rate
     * 
     * @param name
     * @param meter
     */
    private void reportMeter(String name, Meter meter) {
        String prefixedName = prefix(name);
        if (!snapshots.hasDescriptor(prefixedName)) {

            MetricItem.Builder builder = MetricItem.Builder.create();

            builder.events("events");

            builder.events("m1_rate", 1, rateUnit);
            builder.events("m5_rate", 5, rateUnit);
            builder.events("m15_rate", 15, rateUnit);
            builder.events("mean_rate", rateUnit);
            snapshots.setDescriptor(prefixedName, builder.build());
        }

        Map<String, Number> values = new HashMap<String, Number>();

        addMetered(meter, values, "events");

        snapshots.addSnapshot(prefixedName, getTimestamp(), values);
    }

    /**
     * Report a histogram using fields max/mean/min/stddev,p50/p75/p95/p98/p99/p999/count
     * 
     * @param name
     * @param histogram
     */
    private void reportHistogram(String name, Histogram histogram) {
        final Snapshot snapshot = histogram.getSnapshot();
        String prefixedName = prefix(name);
        if (!snapshots.hasDescriptor(prefixedName)) {

            MetricItem.Builder builder = MetricItem.Builder.create();
            builder.count("max");
            builder.count("mean");
            builder.count("min");
            builder.count("stddev");
            builder.count("p50");
            builder.count("p75");
            builder.count("p95");
            builder.count("p98");
            builder.count("p99");
            builder.count("p95");
            builder.count("p999");
            builder.count("count");

            snapshots.setDescriptor(prefixedName, builder.build());

        }

        Map<String, Number> values = new HashMap<String, Number>();

        values.put("max", (snapshot.getMax()));
        values.put("mean", (snapshot.getMean()));
        values.put("min", (snapshot.getMin()));
        values.put("stddev", (snapshot.getStdDev()));
        values.put("p50", (snapshot.getMedian()));
        values.put("p75", (snapshot.get75thPercentile()));
        values.put("p95", (snapshot.get95thPercentile()));
        values.put("p98", (snapshot.get98thPercentile()));
        values.put("p99", (snapshot.get99thPercentile()));
        values.put("p999", (snapshot.get999thPercentile()));
        values.put("count", histogram.getCount());

        snapshots.addSnapshot(prefixedName, getTimestamp(), values);
    }

    private void reportCounter(String name, Counter counter) {
        String prefixedName = prefix(name);

        if (!snapshots.hasDescriptor(prefixedName)) {
            snapshots.setDescriptor(prefixedName, MetricItem.Builder.create().count("count").build());
        }
        long timestamp = getTimestamp();

        snapshots.addSnapshot(prefixedName, timestamp, map("count", counter.getCount()));
    }

    private long getTimestamp() {
        return clock.getTime();
    }

    /**
     * Report a gauge using the field gauge. Only numeric values are used.
     * 
     * @param name
     * @param gauge
     */
    private void reportGauge(String name, Gauge gauge) {
        String prefixedName = prefix(name);

        Object value = gauge.getValue();
        if (value instanceof Number) {

            if (!snapshots.hasDescriptor(prefixedName)) {
                snapshots.setDescriptor(prefixedName, MetricItem.Builder.create().count("gauge").build());
            }
            long timestamp = getTimestamp();

            snapshots.addSnapshot(prefixedName, timestamp, (Map) map("gauge", value));
        }
    }

    private <K, V> Map<K, V> map(K key, V value) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    private String prefix(String name) {
        return name(prefix, name);
    }

    private void addMetered(Metered meter, Map<String, Number> values, String eventName) {
        values.put(eventName, meter.getCount());

        values.put("m1_rate", convertRate(meter.getOneMinuteRate()));
        values.put("m5_rate", convertRate(meter.getFiveMinuteRate()));
        values.put("m15_rate", convertRate(meter.getFifteenMinuteRate()));
        values.put("mean_rate", convertRate(meter.getMeanRate()));
    }
}
