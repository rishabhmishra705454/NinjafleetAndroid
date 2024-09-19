package com.example.ninjafleet.models;

public class LoginModel {
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

    public class Data {
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

    public class User {
        public int id;
        public String farmerName;
        public String mobileNo;
        public String aadharNo;
        public String address;
        public double totalLand;  // Updated from int to double
        public String landType;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getTotalLand() {  // Changed return type to double
            return totalLand;
        }

        public void setTotalLand(double totalLand) {  // Updated setter method to accept double
            this.totalLand = totalLand;
        }

        public String getLandType() {
            return landType;
        }

        public void setLandType(String landType) {
            this.landType = landType;
        }
    }
}
