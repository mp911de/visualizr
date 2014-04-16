package biz.paluch.visualizr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 13.04.14 11:23
 */
public class ChartDataset {
    private List<ChartData> datasets = new ArrayList<>();

    public List<ChartData> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<ChartData> datasets) {
        this.datasets = datasets;
    }
}
