package com.example.habous.Views.User.UserAndAdmin;

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
import com.example.habous.Helpers.SessionManager;
import com.example.habous.LoginActivity;
import com.example.habous.Models.Utilisateur;
import com.example.habous.R;
import com.example.habous.Views.Admin.HomeAdminActivity;
import com.example.habous.Views.Admin.ListeUtilisateursActivity;
import com.example.habous.Views.Admin.RegisterActivity;
import com.example.habous.Views.User.DetailUserActivity;
import com.example.habous.Views.User.HomeUserActivity;
import com.example.habous.Views.User.ListeCandidatsActivity;
import com.example.habous.Views.User.ModifierCandidatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private String filePath;
    ImageView imageView;
    TextView textView;
    EditText etDate;
    Spinner genrespin;
    ArrayAdapter<String> genreAdapter;
    EditText txtnom;
    EditText txtprenom;
    EditText txtcin;
    String nom;
    String prenom;
    String pdp;
    String datenaiss;
    String sexe;
    String cin;
    String role;
    String login;
    String pass;
    int postion;
    Utilisateur usr ;
    String salt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            GetInfo();
        }catch(Exception e)
        {
            finish();
        }

        setContentView(R.layout.activity_profile);
        genrespin = findViewById(R.id.genrespin);
        txtnom = findViewById(R.id.txtnom);
        txtprenom = findViewById(R.id.txtprenom);
        txtcin = findViewById(R.id.txtcin);
        etDate = findViewById(R.id.txtdatenaiss);
        imageView =  findViewById(R.id.pdpImageView);
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
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = day +"/"+month+"/"+year;
                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });




    }
    public void Enregistrer(View view) {
        nom = txtnom.getText().toString().trim();
        prenom = txtprenom.getText().toString().trim();
        datenaiss = etDate.getText().toString().trim();
        cin = txtcin.getText().toString().trim();
        pdp = usr.getPdp();
        role = usr.getRole();
        login = usr.getLogin();
        pass = usr.getPassword();
        sexe = genrespin.getSelectedItem().toString();
        JSONObject js = new JSONObject();
        try {
            js.put("id", usr.getId());
            js.put("nom", nom);
            js.put("prenom",prenom);
            js.put("pdp", pdp);
            js.put("datenaissance",datenaiss  );
            js.put("sexe", sexe);
            js.put("cin", cin);
            js.put("role", role);
            js.put("login", login);
            js.put("password", pass);
            js.put("salt", salt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, APIServices.URL_BASE+APIServices.URL_USER + usr.getId(), js,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("id"))
                                Toast.makeText(ProfileActivity.this,"Modifié avec succès",Toast.LENGTH_LONG);
                            else
                                Toast.makeText(ProfileActivity.this,"Impossible de de connécté",Toast.LENGTH_LONG);
                        } catch (Exception e) {
                            Toast.makeText(ProfileActivity.this,"Erreur du serveur",Toast.LENGTH_LONG);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        Toast.makeText(getApplicationContext(), "Modifié", Toast.LENGTH_SHORT).show();
        finish();
    }
    public  void getBitmapFromURL(String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_USER +APIServices.URL_GET_UTILISATEUR_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Le candidat n'as pas de photo", Toast.LENGTH_LONG);
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
    public void GetInfo() {
        SessionManager session = new SessionManager(getApplicationContext());
        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_USER + session.pref.getLong("id",0), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    if(json.has("error"))
                        Toast.makeText(ProfileActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();
                    else
                        usr = new Utilisateur(
                                json.getLong("id"),
                                json.getString("nom"),
                                json.getString("prenom"),
                                json.getString("pdp"),
                                json.getString("datenaissance"),
                                json.getString("sexe"),
                                json.getString("cin"),
                                json.getString("role"),
                                json.getString("login"),
                                json.getString("password"),
                                json.getString("sessionToken")
                        );
                    salt = json.getString("salt");
                    getBitmapFromURL(usr.getPdp());
                    txtnom.setText(usr.getNom());
                    txtprenom.setText(usr.getPrenom());
                    etDate.setText(usr.getDatenaissance());
                    if(usr.getSexe().equals("H")){
                        genrespin.setSelection(genreAdapter.getPosition("H"));
                    }else {
                        genrespin.setSelection(genreAdapter.getPosition("F"));
                    }
                    txtcin.setText(usr.getCin());
                }
                catch (JSONException e) {
                    Toast.makeText(ProfileActivity.this,"Erreur du serveur ", Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(ProfileActivity.this,"Verifier vos informations ", Toast.LENGTH_LONG);
                }catch (Exception e)
                {
                    Toast.makeText(ProfileActivity.this,"Une erreur s'est produite", Toast.LENGTH_LONG);
                }
            }
        });
        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);

    }
}
