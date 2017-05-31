package biz.paluch.visualizr.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @author <a href="mailto:stephan.frigger@kaufland.de">Stephan Frigger</a>
 * @since 01.07.14 08:10
 */
public class Snapshots {
    private Map<String, List<TimedSnapshot>> metricsSnapshots = new ConcurrentHashMap<>();
    private Map<String, List<MetricItem>> metricsDescriptors = new ConcurrentHashMap<>();
    private TimeUnit durationUnit = TimeUnit.SECONDS;

    public boolean hasDescriptor(String name) {
        return metricsDescriptors.containsKey(name);
    }

    public void setDescriptor(String name, List<MetricItem> descriptors) {
        metricsDescriptors.put(name, Collections.unmodifiableList(new ArrayList<>(descriptors)));
    }

    public void addSnapshot(String name, long timestamp, Map<String, ? extends Number> values) {

        List<TimedSnapshot> snapshots = getSnapshots(name);

        snapshots.add(new TimedSnapshot(timestamp, (Map) values));
    }

    private List<TimedSnapshot> getSnapshots(String name) {
        return metricsSnapshots.computeIfAbsent(name, k -> new ArrayList<>());
    }

    public List<TimedSnapshot> getSnapshots(String name, long from, long to) {

        List<TimedSnapshot> snapshots = getSnapshots(name);
        List<TimedSnapshot> result = snapshots.stream().filter(it -> it.getTimestamp() >= from && it.getTimestamp() <= to)
                .collect(Collectors.toList());

        return Collections.unmodifiableList(result);
    }

    public TimeUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(TimeUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Map<String, List<MetricItem>> getMetricsDescriptors() {
        return Collections.unmodifiableMap(metricsDescriptors);
    }
}
