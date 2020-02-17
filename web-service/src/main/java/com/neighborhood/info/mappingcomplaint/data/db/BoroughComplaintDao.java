package com.neighborhood.info.mappingcomplaint.data.db;

import com.neighborhood.info.mappingcomplaint.model.Complaint;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoroughComplaintDao {
    private static Connection conn;
    private static List<Complaint> allComplaints = new ArrayList<>();
    private static Map<String, List<Complaint>> complaintByType = new HashMap<>();
    private static Map<String, List<Complaint>> complaintByBorough = new HashMap<>();

    public BoroughComplaintDao(Connection con) {
        if(conn == null){
            this.conn = con;
            load();
        }

    }

    private  void load(){
        allComplaints.addAll(selectAllFromComplaintByBorough());
        complaintByType = allComplaints.stream().collect(Collectors.groupingBy(c->c.getComplaintType()));
        complaintByBorough = allComplaints.stream().collect(Collectors.groupingBy(c->c.getSpatialId()));
    }

    public List<Complaint> selectAllFromComplaintByBorough(){
        List<Complaint> allBrghComplaints = new ArrayList<>();
        try{
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from complaintbyborough");
            while(resultSet.next()){
                Complaint complaint = new Complaint();
                complaint.setSpatialExtent("borough");
                complaint.setSpatialId(resultSet.getString("Borough"));
                complaint.setComplaintType(resultSet.getString("ComplaintType"));
                complaint.setCount(resultSet.getInt("count"));
                if(resultSet.getString("month")==null)continue;
                complaint.setYear(Integer.parseInt(resultSet.getString("month").substring(0,4)));
                complaint.setMonth(Integer.parseInt(resultSet.getString("month").substring(5)));
                allBrghComplaints.add(complaint);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return allBrghComplaints;
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
