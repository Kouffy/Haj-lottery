package com.example.habous.Views.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.Models.Utilisateur;
import com.example.habous.R;
import com.example.habous.Views.Admin.RegisterActivity;
import com.example.habous.Models.UtilisateurAdapter;
import com.example.habous.Views.User.DetailUserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListeUtilisateursActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "ListeUtilisateursActivity";
    ListView listView  ;
    UtilisateurAdapter utilisateurAdapter ;
    Utilisateur utilisateur;
    public static ArrayList<Utilisateur> utilisateurArrayList = new ArrayList<Utilisateur>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_utilisateurs);
        listView = findViewById(R.id.lstUsers);
        utilisateurAdapter = new UtilisateurAdapter(this,utilisateurArrayList);
        listView.setAdapter(utilisateurAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog =  new ProgressDialog(view.getContext());
                CharSequence[] dialogItem = {"Consulter l'utilisateur","Supprimer"};
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0 :startActivity(new Intent(getApplicationContext(), DetailUserActivity.class)
                                    .putExtra("position" ,position));
                                break;
                            case 1: deleteData(utilisateurArrayList.get(position).getId());
                                break;
                        }

                    }
                });
                builder.create().show();

            }
        });
        data();
    }
    private void deleteData(long id ){
        StringRequest request = new StringRequest(Request.Method.DELETE, APIServices.URL_BASE + APIServices.URL_USER +id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject js = new JSONObject(response);
                    if(js.has("id")){
                        Toast.makeText(getApplicationContext(),"Utilisateur supprimé",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ListeUtilisateursActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Erreur du serveur" ,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Erreur du serveur" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    public  void data(){
        StringRequest request = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                utilisateurArrayList.clear();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for( int i = 0; i< jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        utilisateur = new Utilisateur(
                                js.getLong("id"),
                                js.getString("nom"),
                                js.getString("prenom"),
                                js.getString("pdp"),
                                js.getString("datenaissance"),
                                js.getString("sexe"),
                                js.getString("cin"),
                                js.getString("role"),
                                js.getString("login"),
                                js.getString("password"),
                                js.getString("sessionToken")
                        );
                        utilisateurArrayList.add(utilisateur);
                        utilisateurAdapter.notifyDataSetChanged();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(DEBUGTAG,error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void btn_add_activity(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        finish();
    }
}
