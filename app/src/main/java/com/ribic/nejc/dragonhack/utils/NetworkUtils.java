package com.ribic.nejc.dragonhack.utils;


public class NetworkUtils {

    public static String items() {
        return "http://dragonhack.zigastrgar.com/api/items";
    }

    public static String loginUser(String email, String password) {
        String urlTxt = String.format("http://dragonhack.zigastrgar.com/api/login?email=%s&password=%s", email, password);
        return urlTxt;
    }

    public static String registerUser(String email, String password, String name) {
        String urlTxt = String.format("http://dragonhack.zigastrgar.com/api/register?email=%s&password=%s&name=%s", email, password, name);
        return urlTxt;
    }

    public static String orderList(String id){
        return "http://dragonhack.zigastrgar.com/api/orders?user_id=" + id;
    }

}

