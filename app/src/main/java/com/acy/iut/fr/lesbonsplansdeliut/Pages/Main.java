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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Utilisateur;
import com.acy.iut.fr.lesbonsplansdeliut.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class Main extends Activity {

    //static fields for ease of access
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";
    static Utilisateur UserLog = new Utilisateur();

    //graphic interface fields
    private EditText username, password;
    private TextView status;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request_demo);
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
        new AttemptLogin().execute();
    }
    // method when click on MainPage
    public void InscriptionClick(View v){

                Intent intent = new Intent(Main.this, Inscription.class);
                startActivity(intent);
    }

    //convert an inputstream to a string
    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //async call to the php script
    class AttemptLogin extends AsyncTask<Credential, String, JSONObject> {
        private Credential c = new Credential(username.getText().toString(), password.getText().toString());

        //display loading and status
        protected void onPreExecute() {
            status.setText("Connecting...");
            progress.setVisibility(View.VISIBLE);
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
                    String urlParameters = "username=" + c.getUsername() + "&&password=" + c.getPassword()+"&&method="+"LogUser";
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
                    status.setText(result.getString("message"));
                }else{
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
                    Toast.makeText(Main.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent main_to_resultatObjet = new Intent(Main.this, Resultat_recherche.class);
                    startActivity(main_to_resultatObjet);
                }
                progress.setVisibility(View.GONE);
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
