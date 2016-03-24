package com.acy.iut.fr.lesbonsplansdeliut.Pages.Fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.acy.iut.fr.lesbonsplansdeliut.Adapter.RechercheAdapter;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Connection;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Main;
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

/**
 * Created by boinnarr on 16/03/2016.
 */
public class MesObjetsFragment extends Fragment{

    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String URL = "http://rudyboinnard.esy.es/android/";
    private List<Objet> result_List = new ArrayList<Objet>();
    private ListView mes_objets_liste;
    Objet selectedObject;
    ProgressBar maProgressBar;

    public MesObjetsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_mes_objets, container, false);
        maProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarMesObjets);
        new LoadPage().execute();
        mes_objets_liste = (ListView)rootView.findViewById(R.id.my_objects_list);


        String[] mStrings = {""};

//Creation de l'adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mStrings);

//On passe nos donnees au composant ListView
        mes_objets_liste.setAdapter(adapter);
        mes_objets_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parentView, View childView,
                                    int position, long id) {
                selectedObject = (Objet) (mes_objets_liste.getItemAtPosition(position));
                Log.d("DEBUG LIST VIEW CLICK", selectedObject.getNom());
                ((Main) getActivity()).setObjBundl(selectedObject);
                ((Main) getActivity()).displayView(4);
            }

        });

        return rootView;
    }

    //async call to the php script
    class LoadPage extends AsyncTask<Credential, String, JSONObject> {

        //display loading and status
        protected void onPreExecute() {
            maProgressBar.setVisibility(View.VISIBLE);
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
                    String urlParameters = "method=MesObjets" + "&&id_user="+ Connection.UserLog.getId();
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
                            result_List.add(new Objet(
                                                        Integer.parseInt(jArrayID.get(i).toString()),
                                                        Double.parseDouble((jArrayprix_OBJET.get(i).toString())),
                                                        jArrayDESC_OBJET.get(i).toString(),
                                                        jArrayNOM_OBJET.get(i).toString(),
                                                        Integer.parseInt(jArrayID_CAT.get(i).toString()),
                                                        Integer.parseInt(jArrayID_USER.get(i).toString()))
                            );
                        }
                    }

                    System.out.println("Marche");
                    System.out.println(result.getString("nom_objet"));
                    Log.d("DEBUG OBJET AFFICHE", result.toString());
                    // result_List.add(new Objet(result.getString("nom_objet"), result.getString("description_objet"), result.getDouble("prix")));
                    RechercheAdapter adapter = new RechercheAdapter(getActivity(), result_List);
                    maProgressBar.setVisibility(View.INVISIBLE);
                    mes_objets_liste.setAdapter(adapter);
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

}
