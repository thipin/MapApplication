package com.wuttipong.project.mapapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amphoe {
    @SerializedName("amphoe_id")
    @Expose
    private Integer amphoeId;
    @SerializedName("amphoe_name")
    @Expose
    private String amphoeName;

    public Integer getAmphoeId() {
        return amphoeId;
    }

    public void setAmphoeId(Integer amphoeId) {
        this.amphoeId = amphoeId;
    }

    public String getAmphoeName() {
        return amphoeName;
    }

    public void setAmphoeName(String amphoeName) {
        this.amphoeName = amphoeName;
    }
}
