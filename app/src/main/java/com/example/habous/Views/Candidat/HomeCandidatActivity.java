package com.example.habous.Views.Candidat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Helpers.ImageSaver;
import com.example.habous.Helpers.SessionManager;
import com.example.habous.LocalDB.LocalDB;
import com.example.habous.Models.Candidat;
import com.example.habous.R;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeCandidatActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "candidatureActivity";
    TextView txtnom;
    TextView txtprenom;
    TextView txtdaten;
    TextView txtsexe;
    TextView txtcin;
    TextView txtncand;
    TextView txttel;
    ImageView pdpCand;
    String doc;
    String pdpx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_candidat);
        txtnom = findViewById(R.id.detailnom);
        txtprenom = findViewById(R.id.detailprenom);
        txtdaten = findViewById(R.id.detaildaten);
        txtsexe = findViewById(R.id.detailsexe);
        txtcin = findViewById(R.id.detailcin);
        txtncand = findViewById(R.id.detailnbcand);
        txttel = findViewById(R.id.detailtel);
        pdpCand = findViewById(R.id.pdpImageViewCandidat);
        if (isOnline())
            GetInfo();
        else
             GetInfoFromDatabase();

    }
public void GetInfoFromDatabase()
{
    LocalDB database = new LocalDB(HomeCandidatActivity.this);
    Cursor crs = database.getLocalCandidat();
    crs.moveToFirst();
    txtnom.setText(crs.getString(1));
    txtprenom.setText(crs.getString(2));
    txtdaten.setText(crs.getString(4));
    txtsexe.setText(crs.getString(6));
    txtcin.setText(crs.getString(7));
    txtncand.setText(String.valueOf(crs.getInt(9)));
    txttel.setText(crs.getString(10));
    doc = crs.getString(8);
    pdpx = crs.getString(3);
    Bitmap bitmap = ImageSaver.loadImageBitmap(getApplicationContext(),pdpx);
    pdpCand.setImageBitmap(bitmap);
}
    public void GetInfo() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        Long idCandidat = sessionManager.pref.getLong("id", 0);
        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_CANDIDATURE + idCandidat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                Candidat cnd;
                try {
                    json = new JSONObject(response);
                    if(json.has("error")){
                        Toast.makeText(HomeCandidatActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();}
                    else{
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
                        txtnom.setText(cnd.getNom());
                        txtprenom.setText(cnd.getPrenom());
                        txtdaten.setText(cnd.getDatenaissance());
                        txtsexe.setText(cnd.getSexe());
                        txtcin.setText(cnd.getCin());
                        txtncand.setText(String.valueOf(json.getInt("ncandidature")));
                        txttel.setText(cnd.getTel());
                        doc = cnd.getDocuments();
                        getBitmapFromURL(cnd.getPdp());

                    }
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(DEBUGTAG,error.getMessage());
            }
        });
        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);
    }


    public  void getBitmapFromURL(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            pdpCand.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(HomeCandidatActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
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
    public void navToDOCUMENTS(View view) {


            Intent i = new Intent(getApplicationContext(), DocumentsActivity.class);
            i.putExtra("pcin", doc);
            i.putExtra("ppass",doc);
            startActivity(i);


    }

    public void LogOutCand(View view) {
        SessionManager sessionManager = new SessionManager(getApplication());
        LocalDB database = new LocalDB(this);
        database.Trnc(getApplicationContext());
        sessionManager.logoutCandidat();
        finish();
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
