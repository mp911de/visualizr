package biz.paluch.visualizr.metrics;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 03.07.14 07:57
 */
public class DefaultComposites {
    public static List<CompositeDataSource> getComposites() {

        CompositeDataSource bufferpools = getBufferPools();
        CompositeDataSource threadstates = getThreadStates();

        CompositeDataSource memory = getMemory();
        CompositeDataSource gc = getGC();

        return Collections.unmodifiableList(Stream.of(bufferpools, threadstates, memory, gc).collect(Collectors.toList()));

    }

    private static CompositeDataSource getMemory() {
        CompositeDataSource memory = new CompositeDataSource("memory", "Memory");

        ComposedChart total = new ComposedChart("total", "Total", "MB");

        total.addMetricRef("total.init", "gauge", 0.000001);
        total.addMetricRef("total.used", "gauge", 0.000001);
        total.addMetricRef("total.max", "gauge", 0.000001);
        total.addMetricRef("total.committed", "gauge", 0.000001);
        memory.addChart(total);

        ComposedChart heap = new ComposedChart("heap", "Heap", "MB");

        heap.addMetricRef("heap.init", "gauge", 0.000001);
        heap.addMetricRef("heap.used", "gauge", 0.000001);
        heap.addMetricRef("heap.max", "gauge", 0.000001);
        heap.addMetricRef("heap.committed", "gauge", 0.000001);
        memory.addChart(heap);

        ComposedChart nonheap = new ComposedChart("non-heap", "Non-Heap", "MB");

        nonheap.addMetricRef("non-heap.init", "gauge", 0.000001);
        nonheap.addMetricRef("non-heap.used", "gauge", 0.000001);
        nonheap.addMetricRef("non-heap.max", "gauge", 0.000001);
        nonheap.addMetricRef("non-heap.committed", "gauge", 0.000001);
        memory.addChart(nonheap);

        ComposedChart usage = new ComposedChart("usage", "Usage", "%");

        usage.addMetricRef("pools.Code-Cache.usage", "gauge", 100);
        usage.addMetricRef("pools.PS-Eden-Space.usage", "gauge", 100);
        usage.addMetricRef("pools.PS-Old-Gen.usage", "gauge", 100);
        usage.addMetricRef("pools.PS-Perm-Gen.usage", "gauge", 100);
        usage.addMetricRef("pools.PS-Survivor-Space.usage", "gauge", 100);
        usage.addMetricRef("non-heap.usage", "gauge", 100);
        usage.addMetricRef("heap.usage", "gauge", 100);
        memory.addChart(usage);
        return memory;
    }

    private static CompositeDataSource getGC() {
        CompositeDataSource gc = new CompositeDataSource("gc", "GC");

        ComposedChart total = new ComposedChart("times", "Times", "ms");

        total.addMetricRef("PS-MarkSweep.time", "gauge");
        total.addMetricRef("PS-Scavenge.time", "gauge");
        total.addMetricRef("ParNew.time", "gauge");
        total.addMetricRef("ConcurrentMarkSweep.time", "gauge");
        gc.addChart(total);

        ComposedChart heap = new ComposedChart("counts", "Counts");

        heap.addMetricRef("PS-MarkSweep.count", "gauge");
        heap.addMetricRef("PS-Scavenge.count", "gauge");
        total.addMetricRef("ParNew.count", "gauge");
        total.addMetricRef("ConcurrentMarkSweep.count", "gauge");
        gc.addChart(heap);

        return gc;
    }

    private static CompositeDataSource getThreadStates() {
        CompositeDataSource threadstates = new CompositeDataSource("threadstates", "Thread states");

        ComposedChart states = new ComposedChart("states", "Thread state counts");

        states.addMetricRef("new.count", "gauge");
        states.addMetricRef("runnable.count", "gauge");
        states.addMetricRef("blocked.count", "gauge");
        states.addMetricRef("waiting.count", "gauge");
        states.addMetricRef("timed_waiting.count", "gauge");
        states.addMetricRef("terminated.count", "gauge");
        threadstates.addChart(states);

        ComposedChart counts = new ComposedChart("counts", "Thread counts");
        counts.addMetricRef("count", "gauge");
        counts.addMetricRef("daemon.count", "gauge");
        counts.addMetricRef("deadlocks", "gauge");

        threadstates.addChart(counts);
        return threadstates;
    }

    private static CompositeDataSource getBufferPools() {
        CompositeDataSource bufferpools = new CompositeDataSource("bufferpools", "Buffer pools");

        ComposedChart direct = new ComposedChart("direct", "Direct Bufferpools");
        direct.addMetricRef("direct.count", "gauge");
        direct.addMetricRef("direct.used", "gauge");
        direct.addMetricRef("direct.capacity", "gauge");

        bufferpools.addChart(direct);

        ComposedChart mapped = new ComposedChart("mapped", "Mapped Bufferpools");
        mapped.addMetricRef("mapped.count", "gauge");
        mapped.addMetricRef("mapped.used", "gauge");
        mapped.addMetricRef("mapped.capacity", "gauge");

        bufferpools.addChart(mapped);
        return bufferpools;
    }
}
