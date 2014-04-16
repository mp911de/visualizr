package biz.paluch.visualizr;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import biz.paluch.visualizr.demo.DemoChartProvider;
import biz.paluch.visualizr.model.ChartDescriptor;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 13.04.14 11:24
 */
@Path("api/{datasourceId}/charts")
public class ChartResource {

    private ChartProvider chartProvider = new DemoChartProvider();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ChartDescriptor> getDatasets(@PathParam("datasourceId") String datasourceId) {

        List<ChartDescriptor> chartDescriptors = getChartProvider().getChartDescriptors(datasourceId);
        return chartDescriptors;
    }

    public ChartProvider getChartProvider() {
        return chartProvider;
    }
}
