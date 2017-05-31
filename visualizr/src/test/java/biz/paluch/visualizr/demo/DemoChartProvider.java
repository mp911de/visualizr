package biz.paluch.visualizr.demo;

import java.text.SimpleDateFormat;
import java.util.*;

import biz.paluch.visualizr.model.ChartData;
import biz.paluch.visualizr.model.ChartDescriptor;
import biz.paluch.visualizr.model.ChartGraphDescriptor;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @author <a href="mailto:stephan.frigger@kaufland.de">Stephan Frigger</a>
 * @since 15.04.14 08:30
 */
public class DemoChartProvider implements ChartProvider {
    @Override
    public List<ChartDescriptor> getChartDescriptors(String datasourceId) {

        return Arrays.asList(getChartDescriptor("calls"), getChartDescriptor("stuff"), getChartDescriptor("else"));
    }

    private ChartDescriptor getChartDescriptor(String id) {
        ChartDescriptor c1 = new ChartDescriptor();
        c1.setId(id);
        c1.setTitle(id);
        c1.setxAxisTitle("Time");
        c1.setyAxisTitle("Calls/5min");
        c1.setCategoryField("date");
        c1.setPrecision(1);

        ChartGraphDescriptor axisDescriptor1 = new ChartGraphDescriptor("getSource", "Get Source");
        ChartGraphDescriptor axisDescriptor2 = new ChartGraphDescriptor("getMethod", "Get Method");

        c1.getGraphs().add(axisDescriptor1);
        c1.getGraphs().add(axisDescriptor2);
        return c1;
    }

    @Override
    public Map<String, List<ChartData>> getDatasets(String dataSourceId, Date from, Date to, List<String> chartIds) {

        Map<String, List<ChartData>> result = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (String chartId : chartIds) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(from);

            List<ChartData> list = new ArrayList<>();

            double degrees = 0;

            while (cal.getTime().before(to)) {

                degrees++;
                if (degrees > 360) {
                    degrees = 0;
                }
                double radians = Math.toRadians(degrees);
                ChartData chartData = new ChartData();
                chartData.set("date", simpleDateFormat.format(cal.getTime()));
                chartData.set("getSource", (Math.sin(radians)));
                chartData.set("getMethod", (Math.cos(radians)));

                list.add(chartData);

                cal.add(Calendar.MINUTE, 1);
            }

            result.put(chartId, list);

        }

        return result;
    }
}
