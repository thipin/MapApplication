package com.wuttipong.project.mapapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specific {
    @SerializedName("specific_id")
    @Expose
    private Integer specificId;
    @SerializedName("specific_name")
    @Expose
    private String specificName;

    public Integer getSpecificId() {
        return specificId;
    }

    public void setSpecificId(Integer specificId) {
        this.specificId = specificId;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(String specificName) {
        this.specificName = specificName;
    }
}
