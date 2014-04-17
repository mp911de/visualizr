package biz.paluch.visualizr;

import biz.paluch.visualizr.demo.DemoChartProvider;
import biz.paluch.visualizr.model.ChartData;
import biz.paluch.visualizr.spi.ChartProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:24
 */
@Path("api/{datasourceId}/data")
public class ChartDataResource {

    private ChartProvider chartProvider = new DemoChartProvider();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<ChartData>> getDatasets(@DefaultValue("1hr") @QueryParam("from") String from,
            @DefaultValue("now") @QueryParam("to") String to, @QueryParam("tz") @DefaultValue("UTC") String tz,
            @PathParam("datasourceId") String datasourceId, @QueryParam("chartId") List<String> chartIds) throws Exception {

        Calendar fromCalendar = Calendar.getInstance(TimeZone.getTimeZone(tz));
        Calendar toCalendar = Calendar.getInstance(TimeZone.getTimeZone(tz));

        setCalendar(from, tz, fromCalendar);
        setCalendar(to, tz, toCalendar);

        return getChartProvider().getDatasets(datasourceId, fromCalendar.getTime(), toCalendar.getTime(), chartIds);
    }

    private void setCalendar(String timeSpec, String tz, Calendar calendar) throws ParseException {
        if (timeSpec != null) {
            switch (timeSpec) {
                case "now":
                    break;
                case "5min":
                    calendar.add(Calendar.MINUTE, -5);
                    break;
                case "10min":
                    calendar.add(Calendar.MINUTE, -10);
                    break;
                case "30min":
                    calendar.add(Calendar.MINUTE, -30);
                    break;
                case "1hr":
                    calendar.add(Calendar.HOUR, -1);
                    break;
                case "6hr":
                    calendar.add(Calendar.HOUR, -6);
                    break;
                case "12hr":
                    calendar.add(Calendar.HOUR, -12);
                    break;
                case "1day":
                    calendar.add(Calendar.DATE, -1);
                    break;
                default:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(tz));
                    calendar.setTime(simpleDateFormat.parse(timeSpec));
                    break;

            }
        }
    }

    public ChartProvider getChartProvider() {
        return chartProvider;
    }
}
