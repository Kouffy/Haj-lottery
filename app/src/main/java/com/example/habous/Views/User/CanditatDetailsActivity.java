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


public class CanditatDetailsActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "candidatureActivity";
    TextView txtnom,txtprenom,txtdaten,txtsexe,txtcin,txtncand,txttel;
    ImageView pdpCand,passp,cinp;
    int postion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canditat_details);
        txtnom = findViewById(R.id.detailnom);
        txtprenom = findViewById(R.id.detailprenom);
        txtdaten = findViewById(R.id.detaildaten);
        txtsexe = findViewById(R.id.detailsexe);
        txtcin = findViewById(R.id.detailcin);
        txtncand = findViewById(R.id.detailnbcand);
        txttel = findViewById(R.id.detailtel);
        pdpCand = findViewById(R.id.pdpImageViewCandidat);
        passp  = findViewById(R.id.pdpImageViewCandidatpass);
        cinp  = findViewById(R.id.pdpImageViewCandidatcin);
        Intent intent = getIntent();
        postion= intent.getExtras().getInt("position");
        getBitmapFromURL(ListeCandidatsActivity.candidatArrayList.get(postion).getPdp());
        getBitmapFromURL2(ListeCandidatsActivity.candidatArrayList.get(postion).getDocuments());
        getBitmapFromURL3("passport"+ ListeCandidatsActivity.candidatArrayList.get(postion).getDocuments());
        txtnom.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getNom());
        txtprenom.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getPrenom());
        txtdaten.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getDatenaissance());
        txtsexe.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getSexe());
        txtcin.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getCin());
        txtncand.setText(String.valueOf(ListeCandidatsActivity.candidatArrayList.get(postion).getNbcandidature()));
        txttel.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getTel());
    }

    public  void getBitmapFromURL(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            pdpCand.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(CanditatDetailsActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
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
    public  void getBitmapFromURL2(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            cinp.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(CanditatDetailsActivity.this, "Le candidat n'as pas de cin", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        cinp.setImageResource(R.drawable.user_icon);
                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }
    public  void getBitmapFromURL3(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            passp.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(CanditatDetailsActivity.this, "Le candidat n'as pas de passport", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        passp.setImageResource(R.drawable.user_icon);
                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }

}
