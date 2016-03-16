package com.acy.iut.fr.lesbonsplansdeliut.Pages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class AfficheObjet extends Activity {
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String URL = "http://rudyboinnard.esy.es/android/";
    public Objet ob;
    public TextView nom_objet,prix_objet,descriptionObjet,nom_personne,mail_personne;
    public Button supprimer;
    public ImageView image_objet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadPage().execute();
        ob = (Objet)getIntent().getSerializableExtra("Objet");
        setContentView(R.layout.activity_affiche_objet);

        nom_objet = (TextView)(findViewById(R.id.nom_objet));
        prix_objet = (TextView)(findViewById(R.id.prix_objet));
        descriptionObjet = (TextView)(findViewById(R.id.description_objet));
        nom_personne = (TextView)(findViewById(R.id.nom_personne));
        mail_personne = (TextView)(findViewById(R.id.mail_personne));
        supprimer = (Button)findViewById(R.id.supprimer);



        nom_objet.setText(ob.getNom());
        prix_objet.setText(ob.getPrix()+ " euros ");
        descriptionObjet.setText(ob.getDescription());

        if(ob.getId_utilisateur() == Connection.UserLog.getId()){
            supprimer.setVisibility(View.VISIBLE);
            supprimer.setEnabled(true);

        }
        else{
            supprimer.setVisibility(View.INVISIBLE);
            supprimer.setEnabled(false);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_affiche_objet, menu);
        return true;
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

    public void DeleteObjet(View v)
    {
        new DeleteObjet().execute();
    }
    //async call to the php script
    class LoadPage extends AsyncTask<Credential, String, JSONObject> {

        //display loading and status
        protected void onPreExecute() {

        }

        //convert an inputstream to a string
        public String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
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
                    url = new URL(URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    String urlParameters = "method=UtilisateurbyID" + "&&id_user="+ob.getId_utilisateur();
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
                    nom_personne.setText(result.getString("nom")+" "+result.getString("prenom"));
                    mail_personne.setText(result.getString("mail"));
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
    class DeleteObjet extends AsyncTask<Credential, String, JSONObject> {

        //display loading and status
        protected void onPreExecute() {

        }

        //convert an inputstream to a string
        public String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
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
                    url = new URL(URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    String urlParameters = "method=DeleteObjet" + "&&id_objet="+ob.getId();
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
                Toast.makeText(AfficheObjet.this, result.getString("message"),Toast.LENGTH_SHORT).show();
                Intent Affiche_Objet_to_Resultat_Recherche = new Intent(AfficheObjet.this, Main.class);
                startActivity(Affiche_Objet_to_Resultat_Recherche);
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
}
