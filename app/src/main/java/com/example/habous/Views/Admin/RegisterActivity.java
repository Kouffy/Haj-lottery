package com.example.habous.Views.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.APISERVICES.VolleyMultipartRequest;
import com.example.habous.Models.Utilisateur;
import com.example.habous.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String DEBUGTAG = "registerActivity";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    ImageView imageView;
    TextView textView;
    Spinner genrespin,txtrole;
    ArrayAdapter<String> genreAdapter;
    ArrayAdapter<String> rolesAdapter;
    private Utilisateur usr = null;
    EditText txtnom,txtprenom,txtcin,txtlogin,txtpass,etDate;
    String nom,prenom,pdp,datenaiss,sexe,cin,role,login,pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        genrespin = findViewById(R.id.genrespin);
        txtnom = findViewById(R.id.txtnom);
        txtprenom = findViewById(R.id.txtprenom);
        txtcin = findViewById(R.id.txtcin);
        txtrole = findViewById(R.id.txtrole);
        txtlogin = findViewById(R.id.txtlogin);
        txtpass = findViewById(R.id.txtpass);
        etDate = findViewById(R.id.txtdatenaiss);
        Calendar calendar = Calendar.getInstance();
        final int   year = calendar.get(Calendar.YEAR);
        final int  month = calendar.get(Calendar.MONTH);
        final int  day = calendar.get(Calendar.DAY_OF_MONTH);
        List<String> genres = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        genres.add("H");
        genres.add("F");
        roles.add("admin");
        roles.add("user");
        genreAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genrespin.setAdapter(genreAdapter);
        rolesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,roles);
        rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtrole.setAdapter(rolesAdapter);
        etDate.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        imageView =  findViewById(R.id.pdpImageView);
        textView =  findViewById(R.id.pdptextview);
        findViewById(R.id.btnPdp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    showFileChooser();
                }
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selectionner une image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

                    textView.setText("Image Selectionné");
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        RegisterActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public void registerClick(View view) {
        nom = txtnom.getText().toString().trim();
        prenom = txtprenom.getText().toString().trim();
        datenaiss = etDate.getText().toString().trim();
        cin = txtcin.getText().toString().trim();
        pdp = System.currentTimeMillis() + ".png";
        role = txtrole.getSelectedItem().toString();
        login = txtlogin.getText().toString().trim();
        pass = txtpass.getText().toString().trim();
        sexe = genrespin.getSelectedItem().toString();
        JSONObject js = new JSONObject();
        try {
            js.put("nom", nom);
            js.put("prenom",prenom);
            js.put("pdp", pdp);
            js.put("datenaissance",datenaiss  );
            js.put("sexe", sexe);
            js.put("cin", cin);
            js.put("role", role);
            js.put("login", login);
            js.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, APIServices.URL_BASE+APIServices.URL_USER, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("id"))
                                Toast.makeText(RegisterActivity.this,"OK",Toast.LENGTH_SHORT);
                        }catch (Exception e)
                        {
                            Toast.makeText(RegisterActivity.this,"Erreur",Toast.LENGTH_SHORT);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"Erreur",Toast.LENGTH_SHORT);
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
        uploadBitmap(bitmap);
        Toast.makeText(getApplicationContext(), "Utilisateur Ajouté", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIServices.URL_BASE+APIServices.URL_USER+APIServices.URL_USER_IMG_METHOD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            if (response.statusCode == 200) {
                                Toast.makeText(getApplicationContext(), "Utilisateur reçu", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "image non recu", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("file", new DataPart(pdp , getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
