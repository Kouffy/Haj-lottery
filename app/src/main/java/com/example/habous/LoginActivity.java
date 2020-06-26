package com.example.habous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Helpers.SessionManager;
import com.example.habous.Models.Utilisateur;
import com.example.habous.Views.Admin.HomeAdminActivity;
import com.example.habous.Views.Candidat.CandidatLoginActivity;
import com.example.habous.Views.Candidat.HomeCandidatActivity;
import com.example.habous.Views.User.HomeUserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import javax.activation.DataHandler;

public class LoginActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "loginActivity";
    private Utilisateur usr = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onResume() {
        super.onResume();
       SessionManager sessionManager = new SessionManager(getApplicationContext());
       String ctk = sessionManager.pref.getString("sessionToken","off");
        if(sessionManager.isLoggedIn())
        {
            if(sessionManager.getType().equals("CANDIDAT"))
            {
                startActivity(new Intent(getApplicationContext(), HomeCandidatActivity.class));
            }
            else if (sessionManager.getType().equals("user"))
            {
                startActivity(new Intent(getApplicationContext(), HomeUserActivity.class));
            }
            else if (sessionManager.getType().equals("admin"))
            {
                startActivity(new Intent(getApplicationContext(), HomeAdminActivity.class));
            }
        }

    }

    public void loginClick(View view) {
       EditText txtlogin =  findViewById(R.id.txtemail);
        EditText txtpass = findViewById(R.id.txtpass);
        if(txtlogin.getText().toString().isEmpty()  || txtpass.getText().toString().isEmpty() || txtlogin == null || txtpass == null){
            Toast.makeText(getApplicationContext(),"Entrer Vous information de connexion",Toast.LENGTH_LONG).show();
            return ;
        }
        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_USER + txtlogin.getText() + '/'+txtpass.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    if(json.has("error"))
                        Toast.makeText(LoginActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();
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
                    SessionManager session = new SessionManager(getApplicationContext());
                    session.createLoginSession(usr.getId(),usr.getRole(),usr.getSessionToken(),usr.getNom() +" " +usr.getPrenom(),usr.getPdp(),json.getString("role"));
                    Intent iadmin = new Intent(getApplicationContext(), HomeAdminActivity.class);
                    Intent iuser = new Intent(getApplicationContext(), HomeUserActivity.class);
                    if (usr.getRole().equals("admin") ) {
                        startActivity(iadmin);
                        finish();
                    } else if(usr.getRole().equals("user")) {
                        startActivity(iuser);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Vous n'etes pas autorisé",Toast.LENGTH_LONG);
                    }

                }
                catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"Verifier vos informations de connexion", Toast.LENGTH_LONG);
                }
                Toast.makeText(LoginActivity.this,"Verifier vos informations de connexion", Toast.LENGTH_LONG);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(LoginActivity.this,"Verifier vos informations de connexion", Toast.LENGTH_LONG);
                }catch (Exception e)
                {
                    Toast.makeText(LoginActivity.this,"Une erreur s'est produite", Toast.LENGTH_LONG);
                }
            }
        });
        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);
    }
    public void navToLoginCandidat(View view) {
        Intent i = new Intent(getApplicationContext(), CandidatLoginActivity.class);
        startActivity(i);
        finish();
    }

}
