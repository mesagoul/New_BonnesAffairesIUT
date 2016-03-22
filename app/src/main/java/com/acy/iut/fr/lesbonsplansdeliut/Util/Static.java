package com.acy.iut.fr.lesbonsplansdeliut.Util;

/**
 * Created by chappazt on 22/03/2016.
 */
public class Static {

    public static final String FLAG_SUCCESS = "success";
    public static final String FLAG_MESSAGE = "message";
    public static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
