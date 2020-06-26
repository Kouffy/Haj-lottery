package com.example.habous.Views.Candidat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Helpers.DbBitmap;
import com.example.habous.Helpers.ImageSaver;
import com.example.habous.Helpers.SendEmailService;
import com.example.habous.Helpers.SessionManager;
import com.example.habous.LocalDB.LocalDB;
import com.example.habous.Models.Candidat;
import com.example.habous.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class CandidatLoginActivity extends AppCompatActivity {

    Button btStartScan;
    EditText etCode;
    LocalDB database = new LocalDB(this);
    Candidat cnd = null;
    public static final String DEBUGTAG = "LoginCandidatActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidat_login);
        btStartScan = findViewById(R.id.btn_scan);
        etCode = findViewById(R.id.txtCodeCandidat);
        btStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });
    }

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Annulé", Toast.LENGTH_LONG).show();
            } else {
                UpdateEditText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void UpdateEditText(String txt){etCode.setText(txt);}

    public void login(View view) {
        String CodeUser = etCode.getText().toString().trim();
        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_CANDIDATURE + APIServices.URL_CANDIDATURE_GET_CODE + CodeUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;

                try {
                    json = new JSONObject(response);
                    if(json.has("error"))
                        Toast.makeText(CandidatLoginActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();
                    else
                        cnd = new Candidat(
                                json.getLong("id"),
                                json.getString("nom"),
                                json.getString("prenom"),
                                json.getString("pdp"),
                                json.getString("datenaissance"),
                                json.getString("code"),
                                json.getString("sexe"),
                                json.getString("cin"),
                                json.getString("documents"),
                                json.getString("tel"),
                                json.getInt("idCandidature"),
                                json.getInt("ncandidature"),
                                json.getString("email")
                        );
                    SessionManager session = new SessionManager(getApplicationContext());
                    session.createLoginSession(cnd.getId(),"CANDIDAT","NOTOKEN",cnd.getNom() + " " + cnd.getPrenom(),cnd.getPdp(),"CANDIDAT");
                    getBitmapFromURL1(cnd.getPdp());
                    getBitmapFromURL2(cnd.getDocuments());
                    getBitmapFromURL3("passport"+cnd.getDocuments());
                    database.Trnc(getApplicationContext());
                    enregistrerLocalement(cnd.getId(),cnd.getNom(),cnd.getPrenom(),cnd.getPdp(),cnd.getDatenaissance(),cnd.getCode(),cnd.getSexe(),cnd.getCin(),cnd.getDocuments(),cnd.getNbcandidature(),cnd.getTel(),cnd.getEmail(),cnd.getIdcandidature());
                    Intent i = new Intent(getApplicationContext(), HomeCandidatActivity.class);
                        startActivity(i);
                        finish();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CandidatLoginActivity.this,"Une Erreur est produite",Toast.LENGTH_LONG).show();
                }
                Log.d(DEBUGTAG,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(CandidatLoginActivity.this,"Veillez verifier votre code",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(CandidatLoginActivity.this,"Une Erreur est produite" + e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
            HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);
    }
    public void  enregistrerLocalement(Long id,String nom,String prenom,String  pdp,String daten,String code,String sexe,String cin,String documents,int ncandidature,String tel,String email,Long idcand){

        if(database.insertData(id,nom,prenom,pdp,daten,code,sexe,cin,documents,ncandidature,tel,email,idcand)){
            Toast.makeText(getApplicationContext(),"Vos donées sont désormais locales" ,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Erreur de base de données" ,Toast.LENGTH_SHORT).show();

        }
    }

    public  void getBitmapFromURL1(final String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            ImageSaver.saveImage(getApplicationContext(),bitmap,src);
                        } else {
                            Toast.makeText(CandidatLoginActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }

    public  void getBitmapFromURL2(final String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_CANDIDATURE_GET_CODE + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            ImageSaver.saveImage(getApplicationContext(),bitmap,src);
                        } else {
                            Toast.makeText(CandidatLoginActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }
    public  void getBitmapFromURL3(final String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_CANDIDATURE_GET_CODE + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            ImageSaver.saveImage(getApplicationContext(),bitmap,src);
                        } else {
                            Toast.makeText(CandidatLoginActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }


}
