package com.acy.iut.fr.lesbonsplansdeliut.Util;

import android.os.AsyncTask;
import android.util.Log;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Utilisateur;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by chappazt on 22/03/2016.
 */
public class JSONRequest extends AsyncTask<Void, String, JSONObject>  {

    private String params;

    public JSONRequest(String urlparams) {
        params = urlparams;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject json = null;
        try {
            Log.d("request!", "starting");
            URL url = null;
            HttpURLConnection connection = null;
            try {
                //initialize connection
                url = new URL(Static.LOGIN_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                String urlParameters = this.params;
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                //write post data to URL
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.write(postData);
                //connect and get data
                connection.connect();
                InputStream in = new BufferedInputStream(connection.getInputStream());
                json = new JSONObject(Static.convertStreamToString(in));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPreExecute() {

    }

    protected void onPostExecute(JSONObject result) {

    }

}
