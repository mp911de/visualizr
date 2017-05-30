package biz.paluch.visualizr.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:20
 */
public class ChartData implements Serializable {

    @JsonIgnore
    private Map<String, Object> data = new HashMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return data;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        data.put(name, value);
    }
}
