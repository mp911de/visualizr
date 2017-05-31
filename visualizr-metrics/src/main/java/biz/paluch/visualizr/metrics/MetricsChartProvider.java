package biz.paluch.visualizr.metrics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.paluch.visualizr.model.ChartData;
import biz.paluch.visualizr.model.ChartDescriptor;
import biz.paluch.visualizr.model.ChartGraphDescriptor;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @author <a href="mailto:stephan.frigger@kaufland.de">Stephan Frigger</a>
 * @since 02.07.14 07:57
 */
public class MetricsChartProvider implements ChartProvider {

    public static final String PERCENTILE_REGEX = "(.*)p(\\d+)";
    public static final String TIMESTAMP = "timestamp";
    public static final String QUANTILES = "quantiles";
    public static final String TIME = "Time";

    private Snapshots snapshots;
    private List<CompositeDataSource> compositeDataSources;

    public MetricsChartProvider(Snapshots snapshots, List<CompositeDataSource> compositeDataSources) {
        this.snapshots = snapshots;
        this.compositeDataSources = Collections.unmodifiableList(new ArrayList<>(compositeDataSources));
    }

    @Override
    public List<ChartDescriptor> getChartDescriptors(String dataSourceId) {

        CompositeDataSource compositeDataSource = getCompositeDatasource(dataSourceId);

        if (compositeDataSource == null) {
            List<MetricItem> descriptors = snapshots.getMetricsDescriptors().get(dataSourceId);
            if (descriptors == null) {
                return Collections.emptyList();
            }
            List<ChartDescriptor> result = getChartDescriptors(descriptors);

            return result;
        }

        List<ChartDescriptor> chartDescriptors = getChartDescriptors(compositeDataSource);

        return chartDescriptors;

    }

    private List<ChartDescriptor> getChartDescriptors(CompositeDataSource compositeDataSource) {
        List<ChartDescriptor> chartDescriptors = new ArrayList<>();
        for (ComposedChart composedChart : compositeDataSource.getCharts()) {

            ChartDescriptor chart = createDescriptor(composedChart.getId(), composedChart.getName(), "",
                    composedChart.getUnits());

            for (MetricRef metricRef : composedChart.getMetrics()) {
                if (!snapshots.hasDescriptor(metricRef.getMetric())) {
                    continue;
                }

                ChartGraphDescriptor graph = new ChartGraphDescriptor();
                graph.setId(valueName(metricRef));

                if (metricRef.getCaption() != null) {
                    graph.setName(metricRef.getCaption());
                } else {
                    graph.setName(metricRef.getMetric());
                }
                chart.getGraphs().add(graph);
            }

            chartDescriptors.add(chart);
        }
        return chartDescriptors;
    }

    private List<ChartDescriptor> getChartDescriptors(List<MetricItem> descriptors) {
        List<ChartDescriptor> result = new ArrayList<>();

        ChartDescriptor percentiles = null;
        ChartDescriptor rates = null;

        for (MetricItem descriptor : descriptors) {

            if (descriptor.getName().matches(PERCENTILE_REGEX)) {

                if (percentiles == null) {
                    percentiles = createDescriptor(QUANTILES, "Quantiles", TIME, descriptor.getUnits());
                }

                ChartGraphDescriptor graph = new ChartGraphDescriptor();
                graph.setId(descriptor.getName());
                graph.setName(descriptor.getDescription());
                percentiles.getGraphs().add(graph);
            } else {

                ChartDescriptor chart = createDescriptor(descriptor.getName(), descriptor.getName(), TIME,
                        descriptor.getUnits());
                ChartGraphDescriptor graph = new ChartGraphDescriptor();
                graph.setId(descriptor.getName());
                graph.setName(descriptor.getDescription());
                chart.getGraphs().add(graph);

                result.add(chart);
            }

        }

        if (percentiles != null) {
            result.add(percentiles);
        }

        if (rates != null) {
            result.add(rates);
        }
        return result;
    }

    private CompositeDataSource getCompositeDatasource(final String datasourceId) {
        return compositeDataSources.stream().filter(input -> datasourceId.equals(input.getId())).findFirst().orElse(null);
    }

    private ChartDescriptor createDescriptor(String id, String title, String xAxisTitle, String unit) {
        ChartDescriptor percentiles;

        percentiles = new ChartDescriptor();
        percentiles.setCategoryField(TIMESTAMP);
        percentiles.setId(id);
        percentiles.setTitle(title);
        percentiles.setxAxisTitle(xAxisTitle);

        percentiles.setyAxisTitle(unit);

        return percentiles;
    }

    @Override
    public Map<String, List<ChartData>> getDatasets(String dataSourceId, Date from, Date to, List<String> chartIds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, List<ChartData>> result = new HashMap<>();

        CompositeDataSource compositeDataSource = getCompositeDatasource(dataSourceId);
        if (compositeDataSource != null) {

            for (ComposedChart composedChart : compositeDataSource.getCharts()) {

                if (!chartIds.contains(composedChart.getId())) {
                    continue;
                }

                List<ChartData> dataset = new ArrayList<>();
                for (MetricRef metricRef : composedChart.getMetrics()) {
                    List<TimedSnapshot> timedSnapshots = snapshots.getSnapshots(metricRef.getMetric(), from.getTime(),
                            to.getTime());

                    for (TimedSnapshot timedSnapshot : timedSnapshots) {

                        String timestamp = simpleDateFormat.format(new Date(timedSnapshot.getTimestamp()));
                        ChartData chartData = getOrCreate(dataset, timestamp);
                        chartData.set(TIMESTAMP, timestamp);

                        Object value = timedSnapshot.getValues().get(metricRef.getValue());
                        if (!(value instanceof Number)) {
                            continue;
                        }

                        Number number = (Number) value;

                        if (metricRef.getMultiplier() != 1) {
                            chartData.set(valueName(metricRef), number.doubleValue() * metricRef.getMultiplier());

                        } else {
                            chartData.set(valueName(metricRef), number);
                        }

                        if (!dataset.contains(chartData)) {
                            dataset.add(chartData);
                        }
                    }
                }

                result.put(composedChart.getId(), dataset);
            }
        } else {

            List<TimedSnapshot> timedSnapshots = snapshots.getSnapshots(dataSourceId, from.getTime(), to.getTime());
            for (String chartId : chartIds) {
                List<ChartData> dataset = getChartDataset(simpleDateFormat, timedSnapshots, chartId, chartId);
                result.put(chartId, dataset);
            }
        }

        return result;
    }

    private String valueName(MetricRef metricRef) {
        return metricRef.getMetric() + "_" + metricRef.getValue();
    }

    private List<ChartData> getChartDataset(SimpleDateFormat simpleDateFormat, List<TimedSnapshot> timedSnapshots,
            String chartId, String valueId) {

        List<ChartData> dataset = new ArrayList<>();

        if (chartId.equals(QUANTILES)) {

            for (TimedSnapshot timedSnapshot : timedSnapshots) {
                String timestamp = simpleDateFormat.format(new Date(timedSnapshot.getTimestamp()));
                ChartData chartData = getOrCreate(dataset, timestamp);
                chartData.set(TIMESTAMP, simpleDateFormat.format(new Date(timedSnapshot.getTimestamp())));

                for (Map.Entry<String, Number> entry : timedSnapshot.getValues().entrySet()) {
                    if (entry.getKey().matches(PERCENTILE_REGEX)) {
                        chartData.set(entry.getKey(), entry.getValue());
                    }
                }
                chartData.set(chartId, timedSnapshot.getValues().get(valueId));
                if (!dataset.contains(chartData)) {
                    dataset.add(chartData);
                }
            }

        } else {

            for (TimedSnapshot timedSnapshot : timedSnapshots) {
                String timestamp = simpleDateFormat.format(new Date(timedSnapshot.getTimestamp()));
                ChartData chartData = getOrCreate(dataset, timestamp);
                chartData.set(TIMESTAMP, simpleDateFormat.format(new Date(timedSnapshot.getTimestamp())));
                chartData.set(chartId, timedSnapshot.getValues().get(valueId));
                if (!dataset.contains(chartData)) {
                    dataset.add(chartData);
                }
            }

        }
        return dataset;
    }

    private ChartData getOrCreate(List<ChartData> dataset, String timestamp) {
        for (ChartData chartData : dataset) {
            if (chartData.getData().containsKey(TIMESTAMP) && chartData.getData().get(TIMESTAMP).equals(timestamp)) {
                return chartData;
            }
        }
        return new ChartData();
    }

}
