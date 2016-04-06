package com.acy.iut.fr.lesbonsplansdeliut.Pages.Fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Connection;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Main;
import com.acy.iut.fr.lesbonsplansdeliut.R;
import com.acy.iut.fr.lesbonsplansdeliut.Util.UtilFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AfficheObjetFragment extends UtilFragment {

    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String URL = "http://rudyboinnard.esy.es/android/";
    public Objet ob = new Objet(1001, 0, "null", "null", 1, 1);
    public TextView nom_objet,prix_objet,descriptionObjet,nom_personne,mail_personne;
    private String mailContact;
    public Button supprimer, contacter;
    public ImageView image_objet;
    private ProgressBar progress;

    public AfficheObjetFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_affiche_objet, container, false);


        ob = (Objet) getArguments().getSerializable("objet");

        nom_objet = (TextView)(rootView.findViewById(R.id.nom_objet));
        prix_objet = (TextView)(rootView.findViewById(R.id.prix_objet));
        descriptionObjet = (TextView)(rootView.findViewById(R.id.description_objet));
        nom_personne = (TextView)(rootView.findViewById(R.id.nom_personne));
        mail_personne = (TextView)(rootView.findViewById(R.id.mail_personne));
        supprimer = (Button)(rootView.findViewById(R.id.supprimer));
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteObjet().execute();
            }
        });
        contacter = (Button)(rootView.findViewById(R.id.contact));
        contacter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] emails = {Connection.UserLog.getMail().toString()};
                String subject = "Votre Bonne Affaire";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, emails);
                email.putExtra(Intent.EXTRA_SUBJECT, subject);

                // need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        progress = (ProgressBar)(rootView.findViewById(R.id.progress));
        progress.setVisibility(View.INVISIBLE);
        new LoadPage().execute();



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
        return rootView;
    }

    //async call to the php script
    class LoadPage extends AsyncTask<Credential, String, JSONObject> {

        //display loading and status
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);

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
                java.net.URL url = null;
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
                Log.d("result",result + "");
                success = result.getInt(FLAG_SUCCESS);
                if(success == 0){
                    System.out.println("Marchepas");
                }else{
                    mailContact = result.getString("mail");
                    nom_personne.setText(result.getString("nom")+" "+result.getString("prenom"));
                    mail_personne.setText(result.getString("mail"));

                }
                progress.setVisibility(View.INVISIBLE);
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
                Toast.makeText(getActivity(), result.getString("message"), Toast.LENGTH_SHORT).show();
                ((Main) getActivity()).displayView(0);

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
    public Boolean backAction(){
        Fragment fragment = getActivity().getFragmentManager().findFragmentByTag("recherche");

        FragmentManager.BackStackEntry backEntry=getFragmentManager().getBackStackEntryAt(getActivity().getFragmentManager().getBackStackEntryCount()-2);
        String lastFragmentName = backEntry.getName();
        if (lastFragmentName == "recherche"){
            ((Main)getActivity()).displayView(0);
        }else{
            ((Main)getActivity()).displayView(3);
        }
        return true;
    }



}
