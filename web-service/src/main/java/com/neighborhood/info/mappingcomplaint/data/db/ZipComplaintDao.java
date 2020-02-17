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

public class ZipComplaintDao {
    private static Connection conn;
    private static List<Complaint> allComplaints = new ArrayList<>();
    private static Map<String, List<Complaint>> complaintByType = new HashMap<>();
    private static Map<String, List<Complaint>> complaintByZips = new HashMap<>();

    public ZipComplaintDao(Connection con) {
        if(conn == null){
            conn = con;
            load();
        }

    }

    private  void load(){
        allComplaints.addAll(selectAllComplaintZips());
        complaintByType = allComplaints.stream().collect(Collectors.groupingBy(c->c.getComplaintType()));
        complaintByZips = allComplaints.stream().filter(c->c.getSpatialId() != null).collect(Collectors.groupingBy(c->c.getSpatialId()));
    }
    public List<Complaint> selectAllComplaintZips(){
        List<Complaint> allComplaints = new ArrayList<>();
        try{
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from complaintbyzip");
            while(resultSet.next()){
                Complaint complaint = new Complaint();
                complaint.setSpatialExtent("zip");
                complaint.setSpatialId(resultSet.getString("IncidentZip"));
                complaint.setComplaintType(resultSet.getString("ComplaintType"));
                complaint.setCount(resultSet.getInt("count"));
                complaint.setYear(Integer.parseInt(yearPart(resultSet.getString("month"))));
                complaint.setMonth(Integer.parseInt(monthPart(resultSet.getString("month"))));
                allComplaints.add(complaint);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return allComplaints;
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

    private String monthPart(String month) {
        if(month == null ||  month.indexOf("-") < 0 || month.length()<6){
            return "0";
        }else{
            return month.substring(5);
        }
    }


    private String yearPart(String month) {
        if(month == null ||  month.indexOf("-") < 0){
            return "0";
        }else{
            return month.substring(0,4);
        }
    }

}
