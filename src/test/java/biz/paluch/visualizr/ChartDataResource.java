package biz.paluch.visualizr;

import biz.paluch.visualizr.demo.DemoChartProvider;
import biz.paluch.visualizr.spi.ChartProvider;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 17.04.14 14:43
 */
public class ChartDataResource extends AbstractChartDataResource {
    @Override
    public ChartProvider getChartProvider() {
        return new DemoChartProvider();
    }
}
