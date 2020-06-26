package com.example.habous.Views.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Models.Utilisateur;
import com.example.habous.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class TirageActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "tiragesActivity";
    private Utilisateur usr = null;
    EditText txtnbrt,txtnbat,txtrespo,txtlieu,txtdatetirage;
    String nbrt,nbat,respo,lieu,datetirage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tirage);
        txtnbrt = findViewById(R.id.txtnbrt);
        txtnbat = findViewById(R.id.txtnbat);
        txtrespo = findViewById(R.id.txtrespo);
        txtlieu = findViewById(R.id.txtlieu);
        Calendar calendar = Calendar.getInstance();
        final int   year = calendar.get(Calendar.YEAR);
        final int  month = calendar.get(Calendar.MONTH);
        final int  day = calendar.get(Calendar.DAY_OF_MONTH);
        txtdatetirage = findViewById(R.id.txtdatetirage);
        txtdatetirage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TirageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = day +"/"+month+"/"+year;
                        txtdatetirage.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
    public void enregistrerClick(View view) {
        nbrt = txtnbrt.getText().toString().trim();
        nbat = txtnbat.getText().toString().trim();
        respo = txtrespo.getText().toString().trim();
        lieu = txtlieu.getText().toString().trim();
        datetirage = txtdatetirage.getText().toString().trim();
        JSONObject js = new JSONObject();
        try {
            js.put("nombreRt", parseInt(nbrt));
            js.put("nombreAt",parseInt(nbat));
            js.put("responsables", respo);
            js.put("lieu",lieu  );
            js.put("dateTirage", datetirage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, APIServices.URL_BASE+ APIServices.URL_TIRAGE, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Ajouté avec succès",Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Erreur : "+error.getMessage(),Toast.LENGTH_LONG);
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
        Toast.makeText(getApplicationContext(),"Tirage Ajouté",Toast.LENGTH_LONG);
        finish();
    }
}
