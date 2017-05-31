package biz.paluch.visualizr.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import biz.paluch.visualizr.model.ChartDataSource;
import biz.paluch.visualizr.spi.DataSourceProvider;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 02.07.14 07:58
 */
public class MetricsDataSourceProvider implements DataSourceProvider {
    private Snapshots snapshots;
    private List<CompositeDataSource> compositeDataSources;

    public MetricsDataSourceProvider(Snapshots snapshots, List<CompositeDataSource> compositeDataSources) {
        this.snapshots = snapshots;
        this.compositeDataSources = ImmutableList.copyOf(compositeDataSources);
    }

    @Override
    public List<ChartDataSource> getDataSources() {
        Map<String, List<MetricItem>> metricsDescriptors = snapshots.getMetricsDescriptors();
        List<ChartDataSource> result = new ArrayList<>();

        List<String> metricIds = new ArrayList<>(metricsDescriptors.keySet());
        List<String> toRemove = new ArrayList<>();

        for (CompositeDataSource compositeDataSource : compositeDataSources) {
            ChartDataSource chartDataSource = new ChartDataSource();
            chartDataSource.setId(compositeDataSource.getId());
            chartDataSource.setName(compositeDataSource.getName());

            boolean hasAnyReference = false;
            for (ComposedChart composedChart : compositeDataSource.getCharts()) {
                for (MetricRef metricRef : composedChart.getMetrics()) {
                    toRemove.add(metricRef.getMetric());
                    if (metricIds.contains(metricRef.getMetric())) {
                        hasAnyReference = true;
                    }
                }
            }

            if (hasAnyReference) {
                result.add(chartDataSource);
            }
        }

        metricIds.removeAll(toRemove);

        for (String metricId : metricIds) {

            ChartDataSource chartDataSource = new ChartDataSource();
            chartDataSource.setId(metricId);
            chartDataSource.setName(metricId);

            result.add(chartDataSource);
        }

        result.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return result;
    }
}
