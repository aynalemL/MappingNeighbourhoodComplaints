package com.neighborhood.info.mappingcomplaint.model;

import java.util.Objects;

public class Complaint {
    private String spatialExtent; //values are borough, zip, parcel
    private String complaintType;
    private String spatialId; //value of boroughName,zipCode, parcelId
    private int count;
    private int year; //year the complaint was reported
    private int month; //month of the year the complaint was reported

    public String getSpatialExtent() {
        return spatialExtent;
    }

    public void setSpatialExtent(String spatialExtent) {
        this.spatialExtent = spatialExtent;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getSpatialId() {
        return spatialId;
    }

    public void setSpatialId(String spatialId) {
        this.spatialId = spatialId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complaint complaint = (Complaint) o;
        return year == complaint.year &&
                month == complaint.month &&
                Objects.equals(spatialExtent, complaint.spatialExtent) &&
                Objects.equals(complaintType, complaint.complaintType) &&
                Objects.equals(spatialId, complaint.spatialId) &&
                Objects.equals(count, complaint.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spatialExtent, complaintType, spatialId, count, year, month);
    }
}
