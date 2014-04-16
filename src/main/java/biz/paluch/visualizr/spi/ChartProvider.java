package biz.paluch.visualizr.spi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import biz.paluch.visualizr.model.ChartData;
import biz.paluch.visualizr.model.ChartDescriptor;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 15.04.14 08:25
 */
public interface ChartProvider {

    List<ChartDescriptor> getChartDescriptors(String datasourceId);

    Map<String, List<ChartData>> getDatasets(String datasourceId, Date from, Date to, List<String> chartIds);
}
