package com.example.ninjafleet.models;

import java.util.Date;

public class RegisterSuperModel {

    public boolean success;
    public String message;
    public Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public String token;
        public User user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }




    public class User{
        public int id;
        public String farmerName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFarmerName() {
            return farmerName;
        }

        public void setFarmerName(String farmerName) {
            this.farmerName = farmerName;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getAadharNo() {
            return aadharNo;
        }

        public void setAadharNo(String aadharNo) {
            this.aadharNo = aadharNo;
        }

        public String getAadharFront() {
            return aadharFront;
        }

        public void setAadharFront(String aadharFront) {
            this.aadharFront = aadharFront;
        }

        public Object getAadharBack() {
            return aadharBack;
        }

        public void setAadharBack(Object aadharBack) {
            this.aadharBack = aadharBack;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTotalLand() {
            return totalLand;
        }

        public void setTotalLand(String totalLand) {
            this.totalLand = totalLand;
        }

        public String getLandType() {
            return landType;
        }

        public void setLandType(String landType) {
            this.landType = landType;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public String mobileNo;
        public String aadharNo;
        public String aadharFront;
        public Object aadharBack;
        public String address;
        public String totalLand;
        public String landType;
        public Date updatedAt;
        public Date createdAt;
    }

}
