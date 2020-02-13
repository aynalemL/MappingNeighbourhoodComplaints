package com.neighborhood.info.mappingcomplaint.model;

import java.util.List;

public class GeoDataDetailed {
    private List<String> keys;
    private List<ParcelData> values;

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<ParcelData> getValues() {
        return values;
    }

    public void setValues(List<ParcelData> values) {
        this.values = values;
    }
}
