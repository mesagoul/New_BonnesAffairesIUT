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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.acy.iut.fr.lesbonsplansdeliut.Adapter.RechercheAdapter;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Credential;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Main;
import com.acy.iut.fr.lesbonsplansdeliut.R;
import com.acy.iut.fr.lesbonsplansdeliut.Util.Static;
import com.acy.iut.fr.lesbonsplansdeliut.Util.UtilFragment;

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
 * Created by mesagoul on 23/03/2016.
 */
public class RechercheByFiltersFragment extends UtilFragment {

    public RechercheByFiltersFragment(){}
    private Spinner spinnerCategories;
    private Button recherche;
    EditText motcle;
    private List<Objet> result_List = new ArrayList<Objet>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recherche_filter, container, false);

        spinnerCategories = (Spinner)rootView.findViewById(R.id.Categorie);
        motcle =(EditText) rootView.findViewById(R.id.motcle);
        recherche = (Button)rootView.findViewById(R.id.btn_rechercher);


        fillSpinner(spinnerCategories, Static.listCategories);
        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG ", "Click on  Recherche");
                String urlParameters = "method=Research"+"&&motcle="+motcle.getText().toString()+"&&idCategorieObjet="+(int)(spinnerCategories.getSelectedItemId()+1);
                ((Main) getActivity()).setUrlBundl(urlParameters);
                ((Main) getActivity()).displayView(0);
                Log.d("DEBUG OBJET",result_List.toString());
            }
        });



        return rootView;
    }

    public void fillSpinner(Spinner spinner, List<String> list) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        Log.d("Spinner", "Spinner adaptee");
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("Spinner", "Spinner set");
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Log.d("Spinner", "Spinner fini");
    }

}
