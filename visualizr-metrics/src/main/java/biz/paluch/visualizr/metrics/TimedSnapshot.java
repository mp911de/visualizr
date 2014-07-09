package biz.paluch.visualizr.metrics;

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 01.07.14 08:11
 */
public class TimedSnapshot {
    /**
     * time in milliseconds.
     */
    private long timestamp;
    private Map<String, Number> values;

    public TimedSnapshot(long timestamp, Map<String, Number> values) {
        this.timestamp = timestamp;
        this.values = Collections.unmodifiableMap(values);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Number> getValues() {
        return values;
    }
}
