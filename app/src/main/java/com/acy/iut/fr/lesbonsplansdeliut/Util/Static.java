package com.acy.iut.fr.lesbonsplansdeliut.Util;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chappazt on 22/03/2016.
 */
public class Static {

    public static final String FLAG_SUCCESS = "success";
    public static final String FLAG_MESSAGE = "message";
    public static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";

    public static List<String> listCategories;

    public static List<String> fillCategories() {
        final List<String> temp = new ArrayList<String>();
        new JSONRequest("method=LoadPageAddObject") {
            protected void onPostExecute(JSONObject result) {
                int success = 0;

                try {
                    JSONArray jArrayDept = (result.getJSONArray("nom_categorie"));
                    //JSONArray jArrayid = (result.getJSONArray("id_categorie"));
                    if (jArrayDept != null) {
                        for (int i=0;i<jArrayDept.length();i++){
                            temp.add(jArrayDept.get(i).toString());
                            Log.d("DEBUG", temp.get(i));
                        }
                    }
                    //fillSpinner(spinnerCategories,temp);
                    //alert the user of the status of the connection
                    success = result.getInt(Static.FLAG_SUCCESS);
                    //testText.setText(result.getString(FLAG_MESSAGE)+"");
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                    //e.printStackTrace();
                }
                //log the success status
                if (success == 1) {
                    Log.d("OK", "OK");
                } else {
                    Log.d("Error", "Error");
                }
            }
        }.execute();
        return temp;
    }

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
