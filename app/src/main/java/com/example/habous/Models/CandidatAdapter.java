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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Models.Candidat;
import com.example.habous.R;
import com.example.habous.Views.Candidat.HomeCandidatActivity;


import java.util.List;

public class CandidatAdapter extends ArrayAdapter<Candidat> {
    Context context ;
    List<Candidat> listCandidat ;
    public CandidatAdapter(@NonNull Context context, List<Candidat> listCandidat) {
        super(context, R.layout.costum_item_candidat,listCandidat);
        this.context = context ;
        this.listCandidat = listCandidat;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_item_candidat,null,true);
        TextView costumprenom = view.findViewById(R.id.costumnom);
        ImageView im= view.findViewById(R.id.imgcandidat);
         getBitmapFromURL(listCandidat.get(position).getPdp(),im);
        costumprenom.setText(listCandidat.get(position).getNom());
        return view;
    }
    public  void getBitmapFromURL(String src,final ImageView im) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            im.setImageBitmap(bitmap);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        im.setImageResource(R.drawable.user_icon);
                    }
                });
        HabousVolley.getInstance(getContext()).addToRequestQueue(request);
    }
}
