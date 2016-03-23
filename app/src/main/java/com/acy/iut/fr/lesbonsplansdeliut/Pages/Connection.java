package com.acy.iut.fr.lesbonsplansdeliut.Pages;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Utilisateur;
import com.acy.iut.fr.lesbonsplansdeliut.R;
import com.acy.iut.fr.lesbonsplansdeliut.Util.JSONRequest;
import com.acy.iut.fr.lesbonsplansdeliut.Util.Static;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class Connection extends Activity {

    //static fields for ease of access
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";
    public static Utilisateur UserLog = new Utilisateur();

    //graphic interface fields
    private EditText username, password;
    private TextView status;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request_demo);
        ActionBar actionBar = getActionBar();
        Static.ActionBarColor(actionBar, "#99e6ff");
        Static.ActionBarTitleColor(actionBar, "#666", "Les bonnes affaires de l'IUT");



        //initialize all fields
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        status = (TextView)findViewById(R.id.status);
        progress = (ProgressBar)findViewById(R.id.progressBar);
       progress.setVisibility(View.INVISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.logo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_http_request_demo, menu);

        return true;
    }

    //click method for the login button
    public void loginClick(View v) {
        if(v.getId() != R.id.submit) {
            return;
        }
        progress.setVisibility(View.VISIBLE);
        Credential c = new Credential(username.getText().toString(), password.getText().toString());
        new JSONRequest("username=" + c.getUsername() + "&&password=" + c.getPassword()+"&&method="+"LogUser") {
            protected void onPostExecute(JSONObject result) {
                int success = 0;
                try {
                    //alert the user of the status of the connection
                    success = result.getInt(FLAG_SUCCESS);
                    if(success == 0){
                        status.setText(result.getString("message"));
                    } else {
                        Log.d("DEBUG",result.getString("id"));
                        Log.d("DEBUG",result.getInt("id")+"");

                        UserLog.setId(result.getInt("id"));
                        UserLog.setId_departement(result.getInt("id_departement"));
                        UserLog.setLogin(result.getString("login"));
                        UserLog.setMotdepasse(result.getString("password"));
                        UserLog.setMail(result.getString("mail"));
                        UserLog.setNom(result.getString("nom"));
                        UserLog.setPrenom(result.getString("prenom"));
                        UserLog.setTel(result.getString("telephone"));
                        Log.d("DEBUG USER LOGE",UserLog.toString());
                        status.setText("");
                        Toast.makeText(Connection.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent main_to_resultatObjet = new Intent(Connection.this, Main.class);
                        startActivity(main_to_resultatObjet);
                        progress.setVisibility(View.INVISIBLE);
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
        }.execute();
    }
    // method when click on MainPage
    public void InscriptionClick(View v){

                Intent intent = new Intent(Connection.this, Inscription.class);
                startActivity(intent);
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
