package biz.paluch.visualizr.spi;

import java.util.List;

import biz.paluch.visualizr.model.Datasource;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 15.04.14 08:18
 */
public interface DatasourceProvider {

    List<Datasource> getDatasources();
}
