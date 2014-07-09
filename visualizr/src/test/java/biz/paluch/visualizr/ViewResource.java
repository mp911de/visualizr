package biz.paluch.visualizr;

import biz.paluch.visualizr.metrics.DefaultComposites;
import biz.paluch.visualizr.metrics.MetricsDataSourceProvider;
import biz.paluch.visualizr.metrics.SnapshotsHolder;
import biz.paluch.visualizr.spi.DataSourceProvider;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 17.04.14 14:42
 */
public class ViewResource extends AbstractViewResource {
    @Override
    public DataSourceProvider getDataSourceProvider() {
        return new MetricsDataSourceProvider(SnapshotsHolder.snapshots, DefaultComposites.getComposites());
    }
}
