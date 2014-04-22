package biz.paluch.visualizr.spi;

import biz.paluch.visualizr.model.ChartDataSource;

import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 15.04.14 08:18
 */
public interface DataSourceProvider {

    /**
     * List of data sources.
     * 
     * @return List<ChartDataSource>
     */
    List<ChartDataSource> getDataSources();
}
