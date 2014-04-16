package biz.paluch.visualizr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 13.04.14 11:15
 */
public class DatasourcesRepresentation {
    private List<Datasource> datasources = new ArrayList<>();

    public List<Datasource> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<Datasource> datasources) {
        this.datasources = datasources;
    }
}
