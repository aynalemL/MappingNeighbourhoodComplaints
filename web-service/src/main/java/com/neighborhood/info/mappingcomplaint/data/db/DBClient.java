package com.neighborhood.info.mappingcomplaint.data.db;

import com.neighborhood.info.mappingcomplaint.model.Complaint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBClient {
    private static Connection conn;
    public DBClient(String url, String user, String password) {
        conn=PostgresConnManager.getConnection(url,user,password);
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


}
