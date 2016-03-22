package com.acy.iut.fr.lesbonsplansdeliut.Util;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;

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
    public static void ActionBarColor(ActionBar actionBar, String color){
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
    }
    public static void ActionBarTitleColor(ActionBar actionBar, String color, String name){
        actionBar.setTitle(Html.fromHtml("<font color='" + color + "'>" + name + "</font>"));
    }

}
