package biz.paluch.visualizr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import biz.paluch.visualizr.model.ChartData;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @author <a href="mailto:stephan.frigger@kaufland.de">Stephan Frigger</a>
 * @since 13.04.14 11:24
 */
@Path("api/{datasource}/data")
public abstract class AbstractChartDataResource {

    private final static Map<String, Consumer<Calendar>> CALENDAR_ADJUSTERS;

    static {

        CALENDAR_ADJUSTERS = new HashMap<>();
        CALENDAR_ADJUSTERS.put("now", calendar -> {
        });
        CALENDAR_ADJUSTERS.put("5min", calendar -> calendar.add(Calendar.MINUTE, -5));
        CALENDAR_ADJUSTERS.put("10min", calendar -> calendar.add(Calendar.MINUTE, -10));
        CALENDAR_ADJUSTERS.put("30min", calendar -> calendar.add(Calendar.MINUTE, -30));
        CALENDAR_ADJUSTERS.put("1hr", calendar -> calendar.add(Calendar.HOUR, -1));
        CALENDAR_ADJUSTERS.put("6hr", calendar -> calendar.add(Calendar.HOUR, -6));
        CALENDAR_ADJUSTERS.put("12hr", calendar -> calendar.add(Calendar.HOUR, -12));
        CALENDAR_ADJUSTERS.put("1day", calendar -> calendar.add(Calendar.DATE, -1));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<ChartData>> getDatasets(@DefaultValue("1hr") @QueryParam("from") String from,
            @DefaultValue("now") @QueryParam("to") String to, @QueryParam("tz") @DefaultValue("UTC") String tz,
            @PathParam("datasource") String dataSourceId, @QueryParam("chart") List<String> chartIds) throws Exception {

        Calendar fromCalendar = Calendar.getInstance(TimeZone.getTimeZone(tz));
        Calendar toCalendar = Calendar.getInstance(TimeZone.getTimeZone(tz));

        setCalendar(from, tz, fromCalendar);
        setCalendar(to, tz, toCalendar);

        return getChartProvider().getDatasets(dataSourceId, fromCalendar.getTime(), toCalendar.getTime(), chartIds);
    }

    private void setCalendar(String timeSpec, String tz, Calendar calendar) throws ParseException {
        if (timeSpec == null) {
            return;
        }

        if (CALENDAR_ADJUSTERS.containsKey(timeSpec)) {
            CALENDAR_ADJUSTERS.get(timeSpec).accept(calendar);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(tz));
        calendar.setTime(simpleDateFormat.parse(timeSpec));
    }

    public abstract ChartProvider getChartProvider();
}
