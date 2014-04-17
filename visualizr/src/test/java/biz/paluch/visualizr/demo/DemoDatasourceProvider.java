package biz.paluch.visualizr.demo;

import biz.paluch.visualizr.model.ChartDataSource;
import biz.paluch.visualizr.spi.DataSourceProvider;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 15.04.14 08:20
 */
public class DemoDataSourceProvider implements DataSourceProvider {
    @Override
    public List<ChartDataSource> getDataSources() {
        return Arrays
                .asList(new ChartDataSource("domainservices", "Domain Services"), new ChartDataSource("website", "Website Services"));
    }
}
