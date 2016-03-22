package com.acy.iut.fr.lesbonsplansdeliut.Pages.Fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.acy.iut.fr.lesbonsplansdeliut.Adapter.RechercheAdapter;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.AfficheObjet;
import com.acy.iut.fr.lesbonsplansdeliut.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RechercheFragment extends Fragment {

    //static fields for ease of access
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";
    private int mCurCheckPosition;

    //Déclaration des valeurs necessaire à la création de la listView des resultats de la recherche
    private ListView result_listView;
    private List<Objet> result_List = new ArrayList<Objet>();
    private Objet selectedObject;


    public RechercheFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recherche, container, false);

        //Initialisation de la ListView des resultat de la recherche
        result_listView = (ListView) rootView.findViewById(R.id.result_listView);

        new Research().execute();

        result_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parentView, View childView,
                                    int position, long id) {
                selectedObject = (Objet) (result_listView.getItemAtPosition(position));
                Log.d("DEBUG LIST VIEW CLICK", selectedObject.getNom());

            }

        });

        return rootView;
    }


    //convert an inputstream to a string
    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //async call to the php script
    class Research extends AsyncTask<Credential, String, JSONObject> {

        //display loading and status
        protected void onPreExecute() {

        }

        //Get JSON data from the URL
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(Credential... args) {
            JSONObject json = null;
            try {
                Log.d("request!", "starting");
                URL url = null;
                HttpURLConnection connection = null;
                try {
                    //initialize connection
                    url = new URL(LOGIN_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    String urlParameters = "method=Research";
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                    //write post data to URL
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.write(postData);
                    //connect and get data
                    connection.connect();
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    json = new JSONObject(convertStreamToString(in));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //parse returned data
        protected void onPostExecute(JSONObject result) {
            int success = 0;
            try {
                //alert the user of the status of the connection
                success = result.getInt(FLAG_SUCCESS);
                if (success == 0) {
                    System.out.println("Marchepas");
                } else {

                    JSONArray jArrayID = (result.getJSONArray("id_objet"));
                    JSONArray jArrayID_CAT = (result.getJSONArray("id_categorie"));
                    JSONArray jArrayID_USER = (result.getJSONArray("id_utilisateur"));
                    JSONArray jArrayNOM_OBJET = (result.getJSONArray("nom_objet"));
                    JSONArray jArrayDESC_OBJET = (result.getJSONArray("description_objet"));
                    JSONArray jArrayprix_OBJET = (result.getJSONArray("prix_objet"));
                    if (jArrayID != null) {
                        for (int i = 0; i < jArrayID.length(); i++) {
                            result_List.add(new Objet(Integer.parseInt(jArrayID.get(i).toString()), Double.parseDouble((jArrayprix_OBJET.get(i).toString())), jArrayDESC_OBJET.get(i).toString(), jArrayNOM_OBJET.get(i).toString(), Integer.parseInt(jArrayID_CAT.get(i).toString()), Integer.parseInt(jArrayID_USER.get(i).toString())));
                        }
                    }

                    System.out.println("Marche");
                    System.out.println(result.getString("nom_objet"));
                    Log.d("DEBUG OBJET AFFICHE", result.toString());
                    // result_List.add(new Objet(result.getString("nom_objet"), result.getString("description_objet"), result.getDouble("prix")));
                    RechercheAdapter adapter = new RechercheAdapter(getActivity(), result_List);
                    result_listView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //log the success status
            if (success == 1) {
                Log.d("OK", "OK");
            } else {
                Log.d("Error", "Error");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
}
