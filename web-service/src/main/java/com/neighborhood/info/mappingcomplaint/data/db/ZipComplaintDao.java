package com.neighborhood.info.mappingcomplaint.data.db;

import com.neighborhood.info.mappingcomplaint.model.Complaint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZipComplaintDao {
    private static DBClient dbClient;
    private static List<Complaint> allComplaints = new ArrayList<>();
    private static Map<String, List<Complaint>> complaintByType = new HashMap<>();
    private static Map<String, List<Complaint>> complaintByZips = new HashMap<>();

    public ZipComplaintDao(DBClient client) {
        if(dbClient == null){
            this.dbClient = client;
            load();
        }

    }


    private static void load(){
        allComplaints.addAll(dbClient.selectAllComplaintZips());
        complaintByType = allComplaints.stream().collect(Collectors.groupingBy(c->c.getComplaintType()));
        complaintByZips = allComplaints.stream().filter(c->c.getSpatialId() != null).collect(Collectors.groupingBy(c->c.getSpatialId()));
    }


    public List<Complaint> getAllComplaints(){
        return allComplaints;
    }

    public List<Complaint> getComplaintsForType(String type){
        return complaintByType.get(type);
    }

    public List<Complaint> getComplaintsForZip(String borough){
        return complaintByZips.get(borough);
    }
}
