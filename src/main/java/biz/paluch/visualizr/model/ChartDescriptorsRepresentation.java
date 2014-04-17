package biz.paluch.visualizr.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:16
 */
public class ChartDescriptorsRepresentation {

    private Map<String, ChartDescriptor> chartDescriptors = new HashMap<String, ChartDescriptor>();

    public ChartDescriptorsRepresentation() {
    }

    public ChartDescriptorsRepresentation(List<ChartDescriptor> chartDescriptors) {
        for (ChartDescriptor chartDescriptor : chartDescriptors) {
            this.chartDescriptors.put(chartDescriptor.getId(), chartDescriptor);
        }
    }

    public Map<String, ChartDescriptor> getChartDescriptors() {
        return chartDescriptors;
    }

    public void setChartDescriptors(Map<String, ChartDescriptor> chartDescriptors) {
        this.chartDescriptors = chartDescriptors;
    }
}
