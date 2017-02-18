package com.wuttipong.project.mapapplication.api;

public class ApiUrl {
    public static final String BASEURL = "http://tub.theend3.com/api/";

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

    public static String list_hospital() {
        return BASEURL + "list_hospital.php";
    }

    public static String appove_hospital() {
        return BASEURL + "appove_hospital.php";
    }

    public static String search_hospital() {
        return BASEURL + "search_hospital.php";
    }

    public static String auto_map() {
        return BASEURL + "auto_map.php";
    }

    public static String hospital_detail(int id) {
        return BASEURL + "hospital_detail.php?id=" + id;
    }

    public static String del_hospital(int id) {
        return BASEURL + "del_hospital.php?id=" + id;
    }

}
