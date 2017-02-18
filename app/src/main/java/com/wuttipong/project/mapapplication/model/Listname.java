package com.wuttipong.project.mapapplication.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listname {
    @SerializedName("hospital_id")
    @Expose
    private Integer hospitalId;
    @SerializedName("hospital_name")
    @Expose
    private String hospitalName;
    @SerializedName("hospital_localtion")
    @Expose
    private String hospitalLocaltion;

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalLocaltion() {
        return hospitalLocaltion;
    }

    public void setHospitalLocaltion(String hospitalLocaltion) {
        this.hospitalLocaltion = hospitalLocaltion;
    }
}
