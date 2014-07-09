package biz.paluch.visualizr;

import biz.paluch.visualizr.metrics.DefaultComposites;
import biz.paluch.visualizr.metrics.MetricsChartProvider;
import biz.paluch.visualizr.metrics.SnapshotsHolder;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 17.04.14 14:43
 */
public class ChartResource extends AbstractChartResource {
    @Override
    public ChartProvider getChartProvider() {
        return new MetricsChartProvider(SnapshotsHolder.snapshots, DefaultComposites.getComposites());
    }
}
