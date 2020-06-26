package com.example.habous.Views.Candidat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Helpers.ImageSaver;
import com.example.habous.R;


public class DocumentsActivity extends AppCompatActivity {
ImageView docCin,passim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        docCin = findViewById(R.id.docCinView);
        passim = findViewById(R.id.docPassView);
        Bundle extras = getIntent().getExtras();
        if (!isOnline()){
            String str = extras.getString("pcin");
            String str2 = "passport" + extras.getString("ppass");
        Bitmap bitmap = ImageSaver.loadImageBitmap(getApplicationContext(),str);
        Bitmap bitmap1 = ImageSaver.loadImageBitmap(getApplicationContext(),str2);
        passim.setImageBitmap(bitmap1);
        docCin.setImageBitmap(bitmap);}else {
            getBitmapFromURL(extras.getString("pcin"));
            getBitmapFromURL2(extras.getString("ppass"));
        }

    }

    public  void getBitmapFromURL(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            docCin.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(DocumentsActivity.this, "Le candidat n'as pas de CIN", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        docCin.setImageResource(R.drawable.cardid);
                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }
    public  void getBitmapFromURL2(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + "passport" + src ,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            passim.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(DocumentsActivity.this, "Le candidat n'as pas de PASSPORT", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        passim.setImageResource(R.drawable.passport);
                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }
    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
