package biz.paluch.visualizr.spi;

import biz.paluch.visualizr.model.ChartData;
import biz.paluch.visualizr.model.ChartDescriptor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 15.04.14 08:25
 */
public interface ChartProvider {

    /**
     * Retrieve a list of chart descriptors for a specific data source. A datasource can contain one or more different charts.
     * The chart descriptor id must be unique within a data source.
     * 
     * @param datasourceId
     * @return List<ChartDescriptor>
     */
    List<ChartDescriptor> getChartDescriptors(String datasourceId);

    /**
     * Retrieve chart data.
     * @param dataSourceId
     * @param from
     * @param to
     * @param chartIds
     * @return Map<String, List<ChartData>>
     */
    Map<String, List<ChartData>> getDatasets(String dataSourceId, Date from, Date to, List<String> chartIds);
}
