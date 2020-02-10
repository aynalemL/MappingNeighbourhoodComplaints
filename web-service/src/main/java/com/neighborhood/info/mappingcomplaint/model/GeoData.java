package com.neighborhood.info.mappingcomplaint.model;

import java.util.List;

public class GeoData {
    private List<String> keys;
    private List<Integer> values;
    private List<String> texts;

    public GeoData(List<String> keys, List<Integer> values, List<String> texts) {
        this.keys = keys;
        this.values = values;
        this.texts = texts;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
