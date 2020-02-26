package com.neighborhood.info.mappingcomplaint.data.cache;

import com.neighborhood.info.mappingcomplaint.data.ZipLatLong;
import com.neighborhood.info.mappingcomplaint.data.db.BoroughComplaintDao;
import com.neighborhood.info.mappingcomplaint.data.db.PostgresConnManager;
import com.neighborhood.info.mappingcomplaint.data.db.ZipComplaintDao;
import com.neighborhood.info.mappingcomplaint.model.Complaint;
import com.neighborhood.info.mappingcomplaint.model.DropDown;
import com.neighborhood.info.mappingcomplaint.model.GeoCoordinate;

import java.net.URI;
import java.nio.file.*;
import java.sql.Connection;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DataCache {

    static ZipComplaintDao zipDao;
    static BoroughComplaintDao boroughDao;
    static Set<String> complaintTypes;
    static Map<String, GeoCoordinate> geoCoordinateMap = new HashMap<>();
    private static Connection conn;

    public DataCache(String url, String user, String password) {
        conn= PostgresConnManager.getConnection(url,user,password);


    }


    public DataCache() {

    }

    public DataCache getCache() {
        return this;
    }


    public static void loadCache() {
        zipDao = new ZipComplaintDao(conn);
        boroughDao = new BoroughComplaintDao(conn);
        complaintTypes = boroughDao.getAllComplaints().stream().collect(Collectors.groupingBy(c->c.getComplaintType())).keySet();
        loadGeoCoordinates();
    }

    private static YearMonth createYearMonth(Complaint c) {
        return YearMonth.of(c.getYear(), c.getMonth() == 0 ? 1 : c.getMonth());//TODO I need to check data issue. Month should not be zero
    }

    public List<DropDown> fetchComplaintTypes() {
        List<DropDown> dropDowns = new ArrayList<>();

        for (String key : complaintTypes) {
            dropDowns.add(new DropDown(key.replace(" ", ""), key));
        }
        return dropDowns;
    }

    public Map<YearMonth, List<Complaint>> fetchComplaintTrendByTypeByBorough(String type, String borough) {

        if (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("Select Complaint Type")) {
            return boroughDao.getComplaintsForBorough(borough).stream().collect(Collectors.groupingBy(c -> createYearMonth(c)));
        } else {
            return boroughDao.getComplaintsForTypeForBorough(type,borough).stream().collect(Collectors.groupingBy(c -> createYearMonth(c)));
        }
    }

    public Map<YearMonth, List<Complaint>> fetchByComplaintTrendByTypeByZip(String type, String zip) {
        if (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("Select Complaint Type")) {
            return zipDao.getAllComplaints().stream().filter(c -> c != null && c.getSpatialId() != null && c.getSpatialId().equalsIgnoreCase(zip)).collect(Collectors.groupingBy(c -> createYearMonth(c)));
        } else {
            return zipDao.getComplaintsForType(type).stream().filter(c -> c != null && c.getSpatialId() != null && c.getSpatialId().equalsIgnoreCase(zip)).collect(Collectors.groupingBy(c -> createYearMonth(c)));
        }
    }

    public List<Complaint> fetchComplaintByTypeByZoom(String complaintType, String zoomLevel) {
        if (zoomLevel.equalsIgnoreCase("borough")) {
            if (complaintType.equalsIgnoreCase("all") || complaintType.equalsIgnoreCase("Select Complaint Type")) {
                boroughDao.getAllComplaints();
            } else {
                boroughDao.getComplaintsForType(complaintType);
            }
        } else {
            if (complaintType.equalsIgnoreCase("all") || complaintType.equalsIgnoreCase("Select Complaint Type")) {
                return zipDao.getAllComplaints();
            }else{
                return zipDao.getComplaintsForType(complaintType);
            }

        }
        return boroughDao.getAllComplaints();
    }

    public Map<YearMonth, List<Complaint>> fetchByComplaintTrendByTypeByBorough(String type, String borough) {
        if (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("Select Complaint Type")) {
            return boroughDao.getAllComplaints().stream().filter(c -> c != null && c.getSpatialId() != null && c.getSpatialId().equalsIgnoreCase(borough)).collect(Collectors.groupingBy(c -> createYearMonth(c)));
        } else {
            return boroughDao.getComplaintsForType(type).stream().filter(c -> c != null && c.getSpatialId() != null && c.getSpatialId().equalsIgnoreCase(borough)).collect(Collectors.groupingBy(c -> createYearMonth(c)));
        }
    }
    public static void loadGeoCoordinates(){
        try{
            Arrays.stream(ZipLatLong.zipCodeLatLon.split("\n")).
                    skip(1).forEach(i->addToMap(i));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void addToMap(String line){
        String[] strs = line.split(",");
        String key = strs[0];
        String latitude = strs[1];
        String longitude = strs[2];
        geoCoordinateMap.put(key, new GeoCoordinate(latitude, longitude));
    }
    public GeoCoordinate getCoor(String key){
        return geoCoordinateMap.get(key);
    }
}

