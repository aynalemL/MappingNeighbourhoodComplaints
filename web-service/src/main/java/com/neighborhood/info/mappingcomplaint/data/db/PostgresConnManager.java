package com.neighborhood.info.mappingcomplaint.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class PostgresConnManager {
    private static Connection conn;

    public static Connection getConnection(String url, String user, String pw) {
        try{
            if(conn != null) return conn;

            Class.forName("org.postgresql.Driver");
           //  url = "jdbc:postgresql://localhost:5432/complaintmapper";
            Properties props = new Properties();
            props.setProperty("user",user);
            props.setProperty("password",pw);
            //props.setProperty("ssl","true");
            return DriverManager.getConnection(url, props);
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }


    }


}
