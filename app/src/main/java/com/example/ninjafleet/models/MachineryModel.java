package com.example.ninjafleet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MachineryModel implements Serializable {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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

        @SerializedName("machinery")
        @Expose
        private List<Machinery> machinery;
        @SerializedName("pagination")
        @Expose
        private Pagination pagination;

        public List<Machinery> getMachinery() {
            return machinery;
        }

        public void setMachinery(List<Machinery> machinery) {
            this.machinery = machinery;
        }

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
    }

    public class Machinery implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("providerId")
        @Expose
        private Integer providerId;
        @SerializedName("machineryName")
        @Expose
        private String machineryName;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("model")
        @Expose
        private String model;
        @SerializedName("capacity")
        @Expose
        private String capacity;
        @SerializedName("condition")
        @Expose
        private String condition;
        @SerializedName("fuelType")
        @Expose
        private Object fuelType;
        @SerializedName("pricing")
        @Expose
        private String pricing;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("availabilityStart")
        @Expose
        private String availabilityStart;
        @SerializedName("availabilityEnd")
        @Expose
        private String availabilityEnd;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("images")
        @Expose
        private List<String> images;
        @SerializedName("maintenanceSchedule")
        @Expose
        private String maintenanceSchedule;
        @SerializedName("lastServiceDate")
        @Expose
        private String lastServiceDate;
        @SerializedName("insuranceStatus")
        @Expose
        private Boolean insuranceStatus;
        @SerializedName("usageRestrictions")
        @Expose
        private String usageRestrictions;
        @SerializedName("availableDays")
        @Expose
        private String availableDays;
        @SerializedName("insuranceExpiry")
        @Expose
        private String insuranceExpiry;
        @SerializedName("operatorRequired")
        @Expose
        private Boolean operatorRequired;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getProviderId() {
            return providerId;
        }

        public void setProviderId(Integer providerId) {
            this.providerId = providerId;
        }

        public String getMachineryName() {
            return machineryName;
        }

        public void setMachineryName(String machineryName) {
            this.machineryName = machineryName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getCapacity() {
            return capacity;
        }

        public void setCapacity(String capacity) {
            this.capacity = capacity;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public Object getFuelType() {
            return fuelType;
        }

        public void setFuelType(Object fuelType) {
            this.fuelType = fuelType;
        }

        public String getPricing() {
            return pricing;
        }

        public void setPricing(String pricing) {
            this.pricing = pricing;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getAvailabilityStart() {
            return availabilityStart;
        }

        public void setAvailabilityStart(String availabilityStart) {
            this.availabilityStart = availabilityStart;
        }

        public String getAvailabilityEnd() {
            return availabilityEnd;
        }

        public void setAvailabilityEnd(String availabilityEnd) {
            this.availabilityEnd = availabilityEnd;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getMaintenanceSchedule() {
            return maintenanceSchedule;
        }

        public void setMaintenanceSchedule(String maintenanceSchedule) {
            this.maintenanceSchedule = maintenanceSchedule;
        }

        public String getLastServiceDate() {
            return lastServiceDate;
        }

        public void setLastServiceDate(String lastServiceDate) {
            this.lastServiceDate = lastServiceDate;
        }

        public Boolean getInsuranceStatus() {
            return insuranceStatus;
        }

        public void setInsuranceStatus(Boolean insuranceStatus) {
            this.insuranceStatus = insuranceStatus;
        }

        public String getUsageRestrictions() {
            return usageRestrictions;
        }

        public void setUsageRestrictions(String usageRestrictions) {
            this.usageRestrictions = usageRestrictions;
        }

        public String getAvailableDays() {
            return availableDays;
        }

        public void setAvailableDays(String availableDays) {
            this.availableDays = availableDays;
        }

        public String getInsuranceExpiry() {
            return insuranceExpiry;
        }

        public void setInsuranceExpiry(String insuranceExpiry) {
            this.insuranceExpiry = insuranceExpiry;
        }

        public Boolean getOperatorRequired() {
            return operatorRequired;
        }

        public void setOperatorRequired(Boolean operatorRequired) {
            this.operatorRequired = operatorRequired;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public class Pagination {

        @SerializedName("totalItems")
        @Expose
        private Integer totalItems;
        @SerializedName("currentPage")
        @Expose
        private Integer currentPage;
        @SerializedName("totalPages")
        @Expose
        private Integer totalPages;
        @SerializedName("itemsPerPage")
        @Expose
        private Integer itemsPerPage;

        public Integer getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Integer totalItems) {
            this.totalItems = totalItems;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getItemsPerPage() {
            return itemsPerPage;
        }

        public void setItemsPerPage(Integer itemsPerPage) {
            this.itemsPerPage = itemsPerPage;
        }

    }
}
