package com.example.habous.Views.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.R;
import com.example.habous.Views.User.CanditatDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierCandidatActivity extends AppCompatActivity {
    int stat = 1;
    Spinner genrespin;
    ArrayAdapter<String> genreAdapter;
    ImageView imageView;
    ImageView imageView2;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private String filePath;
    EditText txtnom ;
    EditText txtprenom;
    EditText txtdatenaiss;
    EditText txtcin ;
    EditText txttelephone;
    EditText txtemail ;
    String nom;
    String prenom;
    String datenaiss;
    String code;
    String sexe;
    String cin;
    String documents;
    String telephone;
    String nbcandidature;
    String email;
    String pdp;
    int postion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_candidat);
        //ferme cette page si les inscriptions sont fermées
        getStatus();
        txtnom = findViewById(R.id.txtnom);
        txtprenom = findViewById(R.id.txtprenom);
        txtdatenaiss = findViewById(R.id.txtdatenaiss);
        txtcin = findViewById(R.id.txtcin);
        txttelephone = findViewById(R.id.txttel);
        genrespin = findViewById(R.id.genrespin);
        txtemail = findViewById(R.id.txtemail);
        Calendar calendar = Calendar.getInstance();
        final int   year = calendar.get(Calendar.YEAR);
        final int  month = calendar.get(Calendar.MONTH);
        final int  day = calendar.get(Calendar.DAY_OF_MONTH);
        List<String> genres = new ArrayList<>();
        genres.add("H");
        genres.add("F");
        genreAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genrespin.setAdapter(genreAdapter);
        txtdatenaiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ModifierCandidatActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = day +"/"+month+"/"+year;
                        txtdatenaiss.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        imageView =  findViewById(R.id.pdpImageView);
        imageView2 =  findViewById(R.id.cinImageView);
        Intent intent = getIntent();
        postion= intent.getExtras().getInt("position");
        getBitmapFromURL(ListeCandidatsActivity.candidatArrayList.get(postion).getPdp());
        getBitmapFromURL2(ListeCandidatsActivity.candidatArrayList.get(postion).getDocuments());
        txtnom.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getNom());
        txtprenom.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getPrenom());
        txtdatenaiss.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getDatenaissance());
        if(ListeCandidatsActivity.candidatArrayList.get(postion).getSexe() == "H"){
            genrespin.setSelection(genreAdapter.getPosition("H"));
        }else {
            genrespin.setSelection(genreAdapter.getPosition("F"));
        }
        txtcin.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getCin());
        txttelephone.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getTel());
        txtemail.setText(ListeCandidatsActivity.candidatArrayList.get(postion).getEmail());
    }
    public  void getBitmapFromURL(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_CANDIDATURE +APIServices.URL_GET_CANDIDAT_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(ModifierCandidatActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.user_icon);
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
                            imageView2.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(ModifierCandidatActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView2.setImageResource(R.drawable.cardid);
                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }
    public int getStatus() {

        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_ETAT + 1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;

                try {
                    json = new JSONObject(response);
                    if(json.has("error"))
                        Toast.makeText(ModifierCandidatActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();
                    else
                        stat =  json.getInt("etatinstcription");
                    if(stat == 0){ startActivity(new Intent(getApplicationContext(),HomeUserActivity.class));
                    finish();
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(ModifierCandidatActivity.this,"Problem de connection avec le serveur", Toast.LENGTH_LONG);
                }catch (Exception e)
                {
                    Toast.makeText(ModifierCandidatActivity.this,"Une erreur s'est produite", Toast.LENGTH_LONG);
                }
            }
        });
        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);
        return stat;
    }

    public  void SendCandidat(View view)
    {
        nom = txtnom.getText().toString().trim();
        prenom = txtprenom.getText().toString().trim();
        pdp = ListeCandidatsActivity.candidatArrayList.get(postion).getPdp();
        datenaiss = txtdatenaiss.getText().toString().trim();
        cin = txtcin.getText().toString().trim();
        code = ListeCandidatsActivity.candidatArrayList.get(postion).getCode();
        sexe = genrespin.getSelectedItem().toString();
        documents = ListeCandidatsActivity.candidatArrayList.get(postion).getDocuments();
        telephone = txttelephone.getText().toString().trim();
        nbcandidature = "1";
        email = txtemail.getText().toString().trim();
        JSONObject js = new JSONObject();
        try {
            js.put("id", ListeCandidatsActivity.candidatArrayList.get(postion).getId());
            js.put("nom", nom);
            js.put("prenom",prenom);
            js.put("pdp", pdp);
            js.put("datenaissance",datenaiss  );
            js.put("code", code);
            js.put("sexe", sexe);
            js.put("cin", cin);
            js.put("documents", documents);
            js.put("ncandidature", Integer.parseInt(nbcandidature));
            js.put("tel", telephone);
            js.put("idCandidature", ListeCandidatsActivity.candidatArrayList.get(postion).getIdcandidature());
            js.put("email",email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, APIServices.URL_BASE+APIServices.URL_CANDIDATURE+ ListeCandidatsActivity.candidatArrayList.get(postion).getId(),js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                if(response.has("id"))
                                    Toast.makeText(ModifierCandidatActivity.this,"Modifié avec succès",Toast.LENGTH_LONG);
                                else
                                    Toast.makeText(ModifierCandidatActivity.this,"Impossible de de connécté",Toast.LENGTH_LONG);
                        } catch (Exception e) {
                            Toast.makeText(ModifierCandidatActivity.this,"Erreur du serveur",Toast.LENGTH_LONG);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(ModifierCandidatActivity.this,"Informations erronées",Toast.LENGTH_LONG);
                }catch (Exception e)
                {
                    Toast.makeText(ModifierCandidatActivity.this,"Erreur du serveur",Toast.LENGTH_LONG);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };


        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjReq);
        Toast.makeText(getApplicationContext(),"Candidat Modifié",Toast.LENGTH_SHORT).show();
        finish();

    }
}
