package com.example.ninjafleet.models;

public class BookingRequest {
    private int userId;
    private int machineryId;
    private int providerId;
    private String landCoordinates;
    private String landArea;
    private String startDate;
    private String endDate;
    private String totalAmount;

    public BookingRequest(int userId, int machineryId, int providerId, String landCoordinates, String landArea, String startDate, String endDate, String totalAmount) {
        this.userId = userId;
        this.machineryId = machineryId;
        this.providerId = providerId;
        this.landCoordinates = landCoordinates;
        this.landArea = landArea;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMachineryId() {
        return machineryId;
    }

    public void setMachineryId(int machineryId) {
        this.machineryId = machineryId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getLandCoordinates() {
        return landCoordinates;
    }

    public void setLandCoordinates(String landCoordinates) {
        this.landCoordinates = landCoordinates;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
