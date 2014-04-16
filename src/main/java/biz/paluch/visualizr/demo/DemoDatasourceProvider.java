package biz.paluch.visualizr.demo;

import java.util.Arrays;
import java.util.List;

import biz.paluch.visualizr.model.Datasource;
import biz.paluch.visualizr.spi.DatasourceProvider;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 15.04.14 08:20
 */
public class DemoDatasourceProvider implements DatasourceProvider {
    @Override
    public List<Datasource> getDatasources() {
        return Arrays
                .asList(new Datasource("domainservices", "Domain Services"), new Datasource("website", "Website Services"));
    }
}
