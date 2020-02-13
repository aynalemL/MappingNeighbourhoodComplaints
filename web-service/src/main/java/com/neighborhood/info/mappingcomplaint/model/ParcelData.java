package com.neighborhood.info.mappingcomplaint.model;

import java.util.List;

public class ParcelData {
    private String parcelId;
    private String complaintType;
    private int count;
    private List<String> months;
    private List<Integer> monthlyCount;


    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<Integer> getMonthlyCount() {
        return monthlyCount;
    }

    public void setMonthlyCount(List<Integer> monthlyCount) {
        this.monthlyCount = monthlyCount;
    }
}
