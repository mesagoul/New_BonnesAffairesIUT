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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Utilisateur;
import com.acy.iut.fr.lesbonsplansdeliut.R;
import com.acy.iut.fr.lesbonsplansdeliut.Util.JSONRequest;
import com.acy.iut.fr.lesbonsplansdeliut.Util.Static;

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

public class Inscription extends Activity {

    //static fields for ease of access

    //Declare fields
    private EditText nom,prenom, password,mail,tel,login;
    private TextView testText;
    private ListView list_departement;

    private Spinner spinnerlistDepartement;
    private List<String> departements;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new JSONRequest("method=" + "LoadPageInscription") {
            protected void onPostExecute(JSONObject result) {
                int success = 0;

                try {
                    ArrayList<String>listDept = new ArrayList<String>();

                    JSONArray jArrayDept = (result.getJSONArray("nom_departement"));
                    JSONArray jArrayid = (result.getJSONArray("id_departement"));
                    if (jArrayDept != null) {
                        for (int i=0;i<jArrayDept.length();i++){
                            listDept.add(jArrayDept.get(i).toString());
                            Log.d("DEBUG",listDept.get(i));

                        }
                    }
                    fillSpinner(spinnerlistDepartement,listDept);
                    //alert the user of the status of the connection
                    success = result.getInt(Static.FLAG_SUCCESS);
                    Log.d("DEBUG",(String)result.getString("nom_departement"));
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
        setContentView(R.layout.form_inscription);

        //initialize all fields
        nom = (EditText)findViewById(R.id.nom_user);
        prenom = (EditText)findViewById(R.id.prenom_user);
        mail = (EditText)findViewById(R.id.mail_user);
        tel = (EditText)findViewById(R.id.tel_user);
        password = (EditText)findViewById(R.id.mdp_user);
        login = (EditText)findViewById(R.id.login_user);
        testText = (TextView)findViewById(R.id.testText);
        spinnerlistDepartement = (Spinner)findViewById(R.id.list_dept);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription, menu);
        return true;
    }
    // method when click on formPage
    public void inscriptionClick(View v) {
        if(v.getId() != R.id.btnInscription) {
            return;
        }
        Log.d("DEBUG", "Click on inscription");
        if (nom.getText().toString().matches("") || prenom.getText().toString().matches("")|| mail.getText().toString().matches("") || tel.getText().toString().matches("")||  password.getText().toString().matches("") ||  login.getText().toString().matches("")) {
            Toast.makeText(this, "Remplir tous les champs pour continuer", Toast.LENGTH_SHORT).show();
        }else {
            Utilisateur u = new Utilisateur((int)(spinnerlistDepartement.getSelectedItemId()+1),nom.getText().toString(),prenom.getText().toString(),mail.getText().toString(),tel.getText().toString(),password.getText().toString(),login.getText().toString());
            new JSONRequest("nom=" + u.getNom() + "&&prenom=" + u.getPrenom() + "&&departement=" + u.getId_departement()+ "&&mail=" + u.getMail()+ "&&telephone=" + u.getTel()+ "&&password=" + u.getMotdepasse()+ "&&login=" + u.getLogin()+ "&&method=" + "insertUser") {
                protected void onPostExecute(JSONObject result) {
                    int success = 0;

                    try {
                        //alert the user of the status of the connection
                        success = result.getInt(Static.FLAG_SUCCESS);
                        Toast.makeText(Inscription.this, (String)result.getString(Static.FLAG_MESSAGE),
                                Toast.LENGTH_LONG).show();
                        Intent inscription_to_main = new Intent(Inscription.this, Connection.class);
                        startActivity(inscription_to_main);
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

    public void fillSpinner(Spinner spinner, List<String> list) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        Log.d("Spinner", "Spinner adaptee");
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("Spinner", "Spinner set");
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Log.d("Spinner", "Spinner fini");
    }
}
