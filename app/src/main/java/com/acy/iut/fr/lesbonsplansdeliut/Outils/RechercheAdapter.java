package com.acy.iut.fr.lesbonsplansdeliut.Outils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by boinnarr on 08/03/2016.
 */
public class RechercheAdapter extends ArrayAdapter<Objet> {
    //objets est la liste des models à afficher
    public RechercheAdapter(Context context, List<Objet> objets) {
        super(context, 0, objets);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_recherche,parent, false);
        }

        ObjetViewHolder viewHolder = (ObjetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ObjetViewHolder();
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.prix = (TextView) convertView.findViewById(R.id.prix);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Objet> objets
        Objet objet = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.titre.setText(objet.getNom());
        viewHolder.desc.setText(objet.getDescription());

 // RUDY JE VEUX METTRE CES NOMBRES SANS LA VIRGULE !!
        // http://java.developpez.com/faq/java/?page=langage_chaine#LANGAGE_STRING_nombre_en_chaine_formatee
        DecimalFormat df = new DecimalFormat("0");
        Double prix = new Double(df.format(objet.getPrix()));
        String prix_string = prix.toString();
        viewHolder.prix.setText(prix_string);






        return convertView;
    }

    private class ObjetViewHolder{
        public TextView titre;
        public TextView desc;
        public ImageView photo;
        public TextView prix;
    }
}
