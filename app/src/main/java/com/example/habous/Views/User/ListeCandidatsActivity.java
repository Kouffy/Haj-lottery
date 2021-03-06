package com.example.habous.Views.User;

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
import com.example.habous.Models.Candidat;
import com.example.habous.R;
import com.example.habous.Models.CandidatAdapter;
import com.example.habous.Views.User.CanditatDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListeCandidatsActivity extends AppCompatActivity {
    public static final String DEBUGTAG = "ListeCandidatActivity";
    ListView listView  ;
    CandidatAdapter candidatAdapter ;
    Candidat candidat;
    public static ArrayList<Candidat> candidatArrayList = new ArrayList<Candidat>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_candidats);
        listView = findViewById(R.id.lstCandidats);
        candidatAdapter = new CandidatAdapter(this,candidatArrayList);
        listView.setAdapter(candidatAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                CharSequence[] dialogItem = {"Consulter le candidat","Modifier le candidat","Supprimer"};
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0 :startActivity(new Intent(getApplicationContext(), CanditatDetailsActivity.class)
                                    .putExtra("position" ,position));
                                break;
                            case 1 :startActivity(new Intent(getApplicationContext(), ModifierCandidatActivity.class)
                                    .putExtra("position" ,position));
                                break;
                            case 2: deleteData(candidatArrayList.get(position).getId());
                                break;

                        }

                    }
                });
                builder.create().show();

            }
        });
        data();
    }
    private void deleteData(long code ){
        StringRequest request = new StringRequest(Request.Method.DELETE, APIServices.URL_BASE + APIServices.URL_CANDIDATURE +code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject js = new JSONObject(response);
                    if(js.has("id")){
                        Toast.makeText(getApplicationContext(),"Candidat supprimé",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ListeCandidatsActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Erreur",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
        StringRequest request = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_CANDIDATURE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                candidatArrayList.clear();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for( int i = 0; i< jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        candidat = new Candidat(
                                js.getLong("id"),
                                js.getString("nom"),
                                js.getString("prenom"),
                                js.getString("pdp"),
                                js.getString("datenaissance"),
                                js.getString("code"),
                                js.getString("sexe"),
                                js.getString("cin"),
                                js.getString("documents"),
                                js.getString("tel"),
                                js.getLong("idCandidature"),
                                js.getInt("ncandidature"),
                                js.getString("email")
                        );
                        candidatArrayList.add(candidat);
                        candidatAdapter.notifyDataSetChanged();
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
}
