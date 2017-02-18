package com.wuttipong.project.mapapplication.api;

public class ApiUrl {
    public static final String BASEURL = "http://192.168.1.101/map/api/";

    public static String login() {
        return BASEURL + "login.php";
    }

    public static String type() {
        return BASEURL + "type.php";
    }

    public static String specific() {
        return BASEURL + "specific.php";
    }

    public static String amphoe() {
        return BASEURL + "amphoe.php";
    }

    public static String add_hospital() {
        return BASEURL + "add_hospital.php";
    }


}
