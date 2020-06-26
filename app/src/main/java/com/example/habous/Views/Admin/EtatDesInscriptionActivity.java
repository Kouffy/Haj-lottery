package com.example.habous.Views.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.R;
import com.example.habous.Views.User.CandidatureActivity;
import com.example.habous.Views.User.HomeUserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EtatDesInscriptionActivity extends AppCompatActivity {
    Switch etatSwitch;
    TextView txtSwitch;
    int stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etat_des_inscription);
        etatSwitch = findViewById(R.id.switch2);
        txtSwitch = findViewById(R.id.textViewSwitch);
        etatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                declancherChangement(isChecked);
            }
        });
        getStatus();
    }
    public void declancherChangement(boolean isChecked)
    {
        if (isChecked)
        {
            changeState(1);
            txtSwitch.setText("Inscriptions lancées");
        }
        else
        {
            changeState(0);
            txtSwitch.setText("Inscriptions férmées");
        }
    }
    public void changeState(int val)
    {
            JSONObject js = new JSONObject();
            try {
                js.put("id", 1);
                js.put("etatinstcription", val);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.PUT, APIServices.URL_BASE+APIServices.URL_ETAT + 1, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                    if (response.has("id"))
                                        Toast.makeText(EtatDesInscriptionActivity.this,"Changement Effectué",Toast.LENGTH_LONG);
                                    else
                                        Toast.makeText(EtatDesInscriptionActivity.this,"Erreur de serveur" ,Toast.LENGTH_LONG);
                            } catch (Exception e) {
                                Toast.makeText(EtatDesInscriptionActivity.this,"Erreur" ,Toast.LENGTH_LONG);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast.makeText(EtatDesInscriptionActivity.this,"Erreur de serveur" ,Toast.LENGTH_LONG);
                    }catch (Exception e)
                    {
                        Toast.makeText(EtatDesInscriptionActivity.this,"Erreur de serveur" ,Toast.LENGTH_LONG);
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
    }
    public int getStatus() {

        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_ETAT + 1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;

                try {
                    json = new JSONObject(response);
                    if(json.has("error"))
                        Toast.makeText(EtatDesInscriptionActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();
                    else
                        stat =  json.getInt("etatinstcription");
                    if(stat == 1){
                        etatSwitch.setChecked(true);
                        txtSwitch.setText("Inscriptions Lancées");}
                    else{
                        etatSwitch.setChecked(false);
                        txtSwitch.setText("Inscriptions Fémées");}

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(EtatDesInscriptionActivity.this,"Problem de connection avec le serveur", Toast.LENGTH_LONG);
                }catch (Exception e)
                {
                    Toast.makeText(EtatDesInscriptionActivity.this,"Une erreur s'est produite", Toast.LENGTH_LONG);
                }
            }
        });
        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);
        return stat;
    }
}
