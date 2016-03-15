package com.acy.iut.fr.lesbonsplansdeliut.Pages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.Outils.RechercheAdapter;
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

public class Resultat_recherche extends Activity {

    //static fields for ease of access
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";

    //Déclaration des valeurs necessaire à la création du rechercheDrawer
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    //Déclaration des valeurs necessaire à la création de la listView des resultats de la recherche
    private ListView result_listView;
    private List<Objet> result_List = new ArrayList<Objet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_recherche);
        //Initialisation de la ListView des resultat de la recherche
        result_listView = (ListView) findViewById(R.id.result_listView);

        //Initialisation des variables de la listView du drawer
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
         //       R.layout.drawer_recherche, mPlanetTitles));


        new Research().execute();
    }

    public void AddObjectClick(View v){
        Intent resultat_to_addObj = new Intent(Resultat_recherche.this, AddObject.class);
        startActivity(resultat_to_addObj);
    }


    // http://developer.android.com/training/implementing-navigation/nav-drawer.html
    public void openDrawerClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

    }

    @Override
    public void setTitle(CharSequence title) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultat_recherche, menu);
        return true;
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
                if(success == 0){
                    System.out.println("Marchepas");
                }else{

                    JSONArray jArrayID = (result.getJSONArray("id_objet"));
                    JSONArray jArrayID_CAT = (result.getJSONArray("id_categorie"));
                    JSONArray jArrayID_USER = (result.getJSONArray("id_utilisateur"));
                    JSONArray jArrayNOM_OBJET = (result.getJSONArray("nom_objet"));
                    JSONArray jArrayDESC_OBJET = (result.getJSONArray("description_objet"));
                    JSONArray jArrayprix_OBJET = (result.getJSONArray("prix_objet"));
                    if (jArrayID != null) {
                        for (int i=0;i<jArrayID.length();i++){
                            result_List.add(new Objet(Integer.parseInt(jArrayID.get(i).toString()),Double.parseDouble((jArrayprix_OBJET.get(i).toString())),jArrayDESC_OBJET.get(i).toString(),jArrayNOM_OBJET.get(i).toString(),Integer.parseInt(jArrayID_CAT.get(i).toString()),Integer.parseInt(jArrayID_USER.get(i).toString())));
                        }
                    }

                    System.out.println("Marche");
                    System.out.println(result.getString("nom_objet"));
                    Log.d("DEBUG OBJET AFFICHE", result.toString());
                   // result_List.add(new Objet(result.getString("nom_objet"), result.getString("description_objet"), result.getDouble("prix")));
                    RechercheAdapter adapter = new RechercheAdapter(Resultat_recherche.this, result_List);
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
}
