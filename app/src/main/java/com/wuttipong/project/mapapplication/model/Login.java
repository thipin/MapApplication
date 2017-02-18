
package com.wuttipong.project.mapapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Login {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("user")
    @Expose
    private User user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        @SerializedName("admin_id")
        @Expose
        private int adminId;
        @SerializedName("admin_name")
        @Expose
        private String adminName;
        @SerializedName("admin_lastname")
        @Expose
        private String adminLastname;

        public int getAdminId() {
            return adminId;
        }

        public void setAdminId(int adminId) {
            this.adminId = adminId;
        }

        public String getAdminName() {
            return adminName;
        }

        public void setAdminName(String adminName) {
            this.adminName = adminName;
        }

        public String getAdminLastname() {
            return adminLastname;
        }

        public void setAdminLastname(String adminLastname) {
            this.adminLastname = adminLastname;
        }

    }
}
