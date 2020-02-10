package com.neighborhood.info.mappingcomplaint.data.db;

import com.neighborhood.info.mappingcomplaint.model.Complaint;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoroughComplaintDao {
    private static DBClient dbClient;
    private static List<Complaint> allComplaints = new ArrayList<>();
    private static Map<String, List<Complaint>> complaintByType = new HashMap<>();
    private static Map<String, List<Complaint>> complaintByBorough = new HashMap<>();

    public BoroughComplaintDao(DBClient client) {
        if(dbClient == null){
            this.dbClient = client;
            load();
        }

    }


    private static void load(){
        allComplaints.addAll(dbClient.selectAllFromComplaintByBorough());
        complaintByType = allComplaints.stream().collect(Collectors.groupingBy(c->c.getComplaintType()));
        complaintByBorough = allComplaints.stream().collect(Collectors.groupingBy(c->c.getSpatialId()));
    }


    public List<Complaint> getAllComplaints(){
        return allComplaints;
    }

    public List<Complaint> getComplaintsForType(String type){
        return complaintByType.get(type);
    }

    public List<Complaint> getComplaintsForBorough(String borough){
        return complaintByBorough.get(borough);
    }

    public List<Complaint> getComplaintsForTypeForBorough(String type, String borough){
        return complaintByType.get(type).stream().filter(c->c.getSpatialId().equalsIgnoreCase(borough)).collect(Collectors.toList());
    }


}
