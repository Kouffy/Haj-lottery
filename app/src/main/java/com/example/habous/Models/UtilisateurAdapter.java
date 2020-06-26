package com.example.habous.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.R;
import java.util.List;


public class UtilisateurAdapter extends ArrayAdapter<Utilisateur> {
    Context context ;
    List<Utilisateur> listPlanning ;

    public UtilisateurAdapter(@NonNull Context context, List<Utilisateur> listPlanning) {
        super(context, R.layout.costum_item_utilisateur,listPlanning);
        this.context = context ;
        this.listPlanning = listPlanning;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_item_utilisateur,null,true);
        TextView costumprenom = view.findViewById(R.id.costumprenom);
        ImageView im= view.findViewById(R.id.imguser);
        getBitmapFromURL(listPlanning
                .get(position).getPdp(),im);
        costumprenom.setText(listPlanning.get(position).getNom());

        return view;
    }
    public  void getBitmapFromURL(String src,final ImageView p) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            p.setImageBitmap(bitmap);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        p.setImageResource(R.drawable.user_icon);
                    }
                });
        HabousVolley.getInstance(getContext()).addToRequestQueue(request);
    }
}
