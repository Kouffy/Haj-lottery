package com.example.habous.Views.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.R;
import com.example.habous.Views.Admin.ListeUtilisateursActivity;

public class DetailUserActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "DetailActivity";
    TextView txtnom;
    TextView txtprenom;
    TextView txtdaten;
    TextView txtsexe;
    TextView txtcin;
    TextView txtnrole;
    ImageView pdpCand;
    int postion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        txtnom = findViewById(R.id.detailnom);
        txtprenom = findViewById(R.id.detailprenom);
        txtdaten = findViewById(R.id.detaildaten);
        txtsexe = findViewById(R.id.saykes);
        txtcin = findViewById(R.id.ciyen);
        txtnrole = findViewById(R.id.rowl);
        pdpCand = findViewById(R.id.pdpImageViewUser);
        Intent intent = getIntent();
        postion= intent.getExtras().getInt("position");
        getBitmapFromURL(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getPdp());
        txtnom.setText(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getNom());
        txtprenom.setText(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getPrenom());
        txtdaten.setText(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getDatenaissance());
        txtsexe.setText(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getSexe());
        txtcin.setText(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getCin());
        txtnrole.setText(ListeUtilisateursActivity.utilisateurArrayList.get(postion).getRole());
    }

    public  void getBitmapFromURL(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_USER +APIServices.URL_GET_UTILISATEUR_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            pdpCand.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(DetailUserActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        pdpCand.setImageResource(R.drawable.user_icon);
                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }
}
