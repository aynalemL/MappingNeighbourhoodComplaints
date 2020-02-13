package com.neighborhood.info.mappingcomplaint.service;

import com.neighborhood.info.mappingcomplaint.data.cache.DataCache;
import com.neighborhood.info.mappingcomplaint.model.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapDataService {

    private static DataCache cache;

    public MapDataService() {
        cache = new DataCache().getCache();
    }


    public GeoData fetchComplaintCountByTypeByZoom(String type, String zoomLevel) {
       List<Complaint> complaints = cache.fetchComplaintByTypeByZoom(type, zoomLevel);
        List<String> keys = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        Map<String, List<Complaint>> map = complaints.stream().filter(c->c.getSpatialId()!=null).collect(Collectors.groupingBy(c->c.getSpatialId()));
        for (String key : map.keySet()) {
            keys.add(key);
            int count = 0;
            for (Complaint complaint : map.get(key)) count = count + complaint.getCount();
            counts.add(count);
            texts.add(generateToolTip(key, count));
        }
        return new GeoData(keys, counts, texts);
    }

    private String generateToolTip(String key, int count) {
        return "<b>Key:" + key + "</b><br>" +
                "Count:" + count + "<br>";
    }


    public List<DropDown> fetchComplaintTypes() {
        return cache.fetchComplaintTypes();

    }

    public GeoDataDetailed findComplaintDetailByTypeByZoom(String type, String zoomLevel) {
        GeoDataDetailed geoDataDetailed = new GeoDataDetailed();
        List<Complaint> complaints = cache.fetchComplaintByTypeByZoom(type, zoomLevel);
        List<String> keys = new ArrayList<>();
        List<ParcelData> values = new ArrayList<>();
        List<String> months = new ArrayList<>();
        List<Integer> monthlyCount = new ArrayList<>();
        Map<String, List<Complaint>> map = complaints.stream().collect(Collectors.groupingBy(c->c.getSpatialId()));
        try {
            for (String key : map.keySet()) {
                keys.add(key);
                int totalCount = 0;
                for (Complaint complaint : map.get(key)) {
                    totalCount = totalCount + complaint.getCount();
                    months.add(complaint.getYear() + "-" + complaint.getMonth());
                    monthlyCount.add(complaint.getCount());
                }
                ParcelData data = new ParcelData();
                data.setComplaintType(type);
                data.setCount(totalCount);
                data.setMonths(months);
                data.setMonthlyCount(monthlyCount);
                data.setParcelId(key);
                values.add(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        geoDataDetailed.setKeys(keys);
        geoDataDetailed.setValues(values);
        return geoDataDetailed;
    }

    public GeoData fetchComplaintTrendByTypeForZip(String type, String zip) {
        Map<YearMonth, List<Complaint>> map = cache.fetchByComplaintTrendByTypeByZip(type, zip);
        List<String> keys = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        List<YearMonth> sorted = map.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (YearMonth key : sorted) {
            keys.add(key.toString());
            int count = 0;
            for (Complaint complaint : map.get(key)) count = count + complaint.getCount();
            counts.add(count);
            texts.add(generateToolTip(key.toString(), count));
        }
        return new GeoData(keys, counts, texts);
    }

    public GeoData fetchCompalintByTypeByZoom(String complaintType, String zoomLevel) {
            List<Complaint>  complaints = cache.fetchComplaintByTypeByZoom(complaintType, zoomLevel);
            List<String> keys=new ArrayList<>();
            List<Integer> counts=new ArrayList<>();
            List<String> texts= new ArrayList<>();
            Map<String,List<Complaint>> complaintByZoom= complaints.stream().filter(c->c.getSpatialId()!=null).
                   collect(Collectors.groupingBy(c->c.getSpatialId()));
            for(String key:complaintByZoom.keySet()){
                keys.add(key);
                int count=0;
                for(Complaint complaint:complaintByZoom.get(key))
                    count += complaint.getCount();
                counts.add(count);
               texts.add( generateToolTip(key, count));
            }


        return new GeoData(keys, counts,texts);
    }

    public GeoData fetchComplaintTrendByTypeForBorough(String type, String borough) {
        Map<YearMonth, List<Complaint>> map = cache.fetchByComplaintTrendByTypeByBorough(type, borough);
        List<String> keys = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        List<YearMonth> sorted = map.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (YearMonth key : sorted) {
            keys.add(key.toString());
            int count = 0;
            for (Complaint complaint : map.get(key)) count = count + complaint.getCount();
            counts.add(count);
            texts.add(generateToolTip(key.toString(), count));
        }
        return new GeoData(keys, counts, texts);
    }
}
