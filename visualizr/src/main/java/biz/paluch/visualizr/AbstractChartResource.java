package biz.paluch.visualizr;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import biz.paluch.visualizr.model.ChartDescriptor;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:24
 */
@Path("api/{datasource}/charts")
public abstract class AbstractChartResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ChartDescriptor> getDatasets(@PathParam("datasource") String dataSourceId) {
        return getChartProvider().getChartDescriptors(dataSourceId);
    }

    public abstract ChartProvider getChartProvider();
}
