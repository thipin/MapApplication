package com.wuttipong.project.mapapplication.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HospitalDetail {
    @SerializedName("hospital_id")
    @Expose
    private Integer hospitalId;
    @SerializedName("hospital_name")
    @Expose
    private String hospitalName;
    @SerializedName("hospital_tel")
    @Expose
    private Integer hospitalTel;
    @SerializedName("hospital_web")
    @Expose
    private String hospitalWeb;
    @SerializedName("hospital_localtion")
    @Expose
    private String hospitalLocaltion;
    @SerializedName("hospital_img")
    @Expose
    private String hospitalImg;
    @SerializedName("hospital_status")
    @Expose
    private Integer hospitalStatus;
    @SerializedName("amphoe_id")
    @Expose
    private Integer amphoeId;
    @SerializedName("type_id")
    @Expose
    private Integer typeId;
    @SerializedName("specific_id")
    @Expose
    private Integer specificId;
    @SerializedName("amphoe_name")
    @Expose
    private String amphoeName;
    @SerializedName("specific_name")
    @Expose
    private String specificName;
    @SerializedName("type_name")
    @Expose
    private String typeName;

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

    public Integer getHospitalTel() {
        return hospitalTel;
    }

    public void setHospitalTel(Integer hospitalTel) {
        this.hospitalTel = hospitalTel;
    }

    public String getHospitalWeb() {
        return hospitalWeb;
    }

    public void setHospitalWeb(String hospitalWeb) {
        this.hospitalWeb = hospitalWeb;
    }

    public String getHospitalLocaltion() {
        return hospitalLocaltion;
    }

    public void setHospitalLocaltion(String hospitalLocaltion) {
        this.hospitalLocaltion = hospitalLocaltion;
    }

    public String getHospitalImg() {
        return hospitalImg;
    }

    public void setHospitalImg(String hospitalImg) {
        this.hospitalImg = hospitalImg;
    }

    public Integer getHospitalStatus() {
        return hospitalStatus;
    }

    public void setHospitalStatus(Integer hospitalStatus) {
        this.hospitalStatus = hospitalStatus;
    }

    public Integer getAmphoeId() {
        return amphoeId;
    }

    public void setAmphoeId(Integer amphoeId) {
        this.amphoeId = amphoeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getSpecificId() {
        return specificId;
    }

    public void setSpecificId(Integer specificId) {
        this.specificId = specificId;
    }

    public String getAmphoeName() {
        return amphoeName;
    }

    public void setAmphoeName(String amphoeName) {
        this.amphoeName = amphoeName;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(String specificName) {
        this.specificName = specificName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
