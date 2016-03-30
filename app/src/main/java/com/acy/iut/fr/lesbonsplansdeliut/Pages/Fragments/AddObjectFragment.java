package com.acy.iut.fr.lesbonsplansdeliut.Pages.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.Objets.Utilisateur;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Connection;
import com.acy.iut.fr.lesbonsplansdeliut.Pages.Main;
import com.acy.iut.fr.lesbonsplansdeliut.R;
import com.acy.iut.fr.lesbonsplansdeliut.Util.Static;
import com.acy.iut.fr.lesbonsplansdeliut.Util.UtilFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AddObjectFragment extends UtilFragment {

    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String URL = "http://rudyboinnard.esy.es/android/";
    private ArrayList<String> listCategories = new ArrayList<String>();
    private Spinner spinnerCategories;
    private EditText titreObjet, descriptionObjet, prixObjet;
    private List<String> listImageObjet = new ArrayList<String>();
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView photo1,photo2,photo3,imageX;
    private String photo1_string, photo2_string,photo3_string;
    private boolean photo1ok = false,photo2ok = false,photo3ok = false;
    private Uri filepath;
    private Bitmap photo1_bitmap = null,photo2_bitmap = null,photo3_bitmap= null,photoX_bitmap;
    private Button btnAddObject, btn_photo1,btn_photo2,btn_photo3;

    private View rootView;

    public AddObjectFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_add_object, container, false);
        titreObjet = (EditText) rootView.findViewById(R.id.titreObjet);
        spinnerCategories = (Spinner) rootView.findViewById(R.id.spinnerCategories);
        photo1 = (ImageView) rootView.findViewById(R.id.photo1);
        photo2 = (ImageView) rootView.findViewById(R.id.photo2);
        photo3 = (ImageView) rootView.findViewById(R.id.photo3);
        titreObjet = (EditText) rootView.findViewById(R.id.titreObjet);
        descriptionObjet = (EditText) rootView.findViewById(R.id.descriptionObjet);
        prixObjet = (EditText) rootView.findViewById(R.id.prix);
        btnAddObject = (Button)rootView.findViewById(R.id.button3);
        btnAddObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG OBJECT", "Clicki on  Add OBject");
                new AddObjet().execute();
            }
        });
        btn_photo1 = (Button)(rootView.findViewById(R.id.btnAddPhoto));
        btn_photo2 = (Button)(rootView.findViewById(R.id.btnAddPhoto2));
        btn_photo3 = (Button)(rootView.findViewById(R.id.btnAddPhoto3));
        btn_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClick(v);
            }
        });
        btn_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClick(v);
            }
        });
        btn_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClick(v);
            }
        });


        fillSpinner(spinnerCategories, Static.listCategories);
        Log.d("DEBUG", Connection.UserLog.getLogin());


        return rootView;
    }


    public void AddClick(View v){
        if(v.getId() == R.id.btnAddPhoto){
            imageX = (ImageView)rootView.findViewById(R.id.photo1);
        }else if (v.getId() == R.id.btnAddPhoto2){
            imageX = (ImageView)rootView.findViewById(R.id.photo2);
        }else if (v.getId() == R.id.btnAddPhoto3){
            imageX = (ImageView)rootView.findViewById(R.id.photo3);
        }
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            filepath = data.getData();

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            if(photo1.getDrawable()== null)
                Log.d("DEBUG PHOTO", "Ello C'est vide");
            else
                Log.d("DEBUG PHOTO", "Ello C'est plein");
            photo1_bitmap = BitmapFactory.decodeFile(picturePath);
            imageX.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            try {
                if(photo1.getDrawable() != null)
                {
                    photo1_bitmap =  MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                    photo1ok = true;
                    Log.d("PHOTO 1"," REMPLIE");
                }
                if(photo2.getDrawable() != null)
                {
                    photo2_bitmap =  MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                    photo2ok = true;
                    Log.d("PHOTO 2"," REMPLIE");
                }
                if(photo3.getDrawable() != null)
                {
                    photo3_bitmap =  MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                    photo3ok = true;
                    Log.d("PHOTO 3"," REMPLIE");
                }
            }catch(IOException e ){
                e.printStackTrace();
            }



            // String picturePath contains the path of selected Image
        }
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

    // ---------------------------------- UPLOAD IMAGE -------------------------------------------------------

    // Convert image to String
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    class AddObjet extends AsyncTask<Utilisateur, String, JSONObject> {
        private Objet ob = new Objet(Connection.UserLog.getId(),(int)(spinnerCategories.getSelectedItemId()+1),titreObjet.getText().toString(), descriptionObjet.getText().toString(), Double.parseDouble(prixObjet.getText().toString()));
        //display loading and status
        protected void onPreExecute() {
            Log.d("AddObjet", "Connexion add object start");
        }
        //Get JSON data from the URL
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(Utilisateur... args) {
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
                    if(photo1ok){
                        listImageObjet.add(getStringImage(photo1_bitmap));
                        photo1ok = false;
                    }
                    if(photo2ok)
                    {
                        listImageObjet.add(getStringImage(photo2_bitmap));
                        photo1ok = false;
                    }
                    if(photo3ok)
                    {
                        listImageObjet.add(getStringImage(photo3_bitmap));
                        photo1ok = false;
                    }
                    //ob.setUrl_photo1(listImageObjet);
                    String urlParameters = "nomObjet=" + ob.getNom() + "&&id_user="+ob.getId_utilisateur()+"&&descriptionObjet=" + ob.getDescription() + "&&prixObjet=" + ob.getPrix() + "&&idCategorieObjet=" + ob.getId_categorie() + "&&method=" + "AddObjet";
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

        //parse returned data
        protected void onPostExecute(JSONObject result) {
            int success = 0;

            try {
                Toast.makeText(getActivity(), result.getString("message"), Toast.LENGTH_SHORT).show();
                ((Main) getActivity()).displayView(3);

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
    }


}
