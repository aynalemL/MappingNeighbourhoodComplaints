package com.neighborhood.info.mappingcomplaint.model;

import java.util.List;

public class GeoData {
    private List<String> keys;
    private List<Integer> values;
    private List<String> texts;
    private GeoCoordinate centerCoor;


    public GeoData(List<String> keys, List<Integer> values, List<String> texts, GeoCoordinate centerCoor) {
        this.keys = keys;
        this.values = values;
        this.texts = texts;
        if(centerCoor!=null){
            this.centerCoor = centerCoor;
        }else{
            this.centerCoor = new GeoCoordinate("40.748161","-73.882912");//TODO remove hardcoding, this is default center for NYC
        }
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

    public GeoCoordinate getCenterCoor() {
        return centerCoor;
    }

    public void setCenterCoor(GeoCoordinate centerCoor) {
        this.centerCoor = centerCoor;
    }
}
