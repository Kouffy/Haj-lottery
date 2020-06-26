package com.example.habous.Views.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Helpers.SendEmailService;
import com.example.habous.APISERVICES.VolleyMultipartRequest;
import com.example.habous.Helpers.SessionManager;
import com.example.habous.LoginActivity;
import com.example.habous.Models.Candidat;
import com.example.habous.Models.Utilisateur;
import com.example.habous.R;
import com.example.habous.Views.Admin.HomeAdminActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CandidatureActivity extends AppCompatActivity  {

    int stat = 1 ;
    public static final String DEBUGTAG = "candidatureActivity";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private static final int PICK_CIN_REQUEST =2 ;
    private static final int PICK_PASS_REQUEST =3 ;
    public Boolean isFirstSent = false;
    Spinner genrespin;
    ArrayAdapter<String> genreAdapter;
    ImageView imageView,imageView2,imageView3;
    TextView textView,textView2,textView3;
    private Bitmap bitmap,bitmap2,bitmap3;
    private String filePath,nom,prenom,datenaiss,code,sexe,cin,documents,telephone,nbcandidature,email,pdp,typeCandidature;
    private Long sharedId;
    EditText txtnom,txtprenom,txtdatenaiss, txtcin,txttelephone,txtemail;
    Button btnTerm;
    public void clearFields()
    {
        txtnom.setText("");
        txtprenom.setText("");
        txtdatenaiss.setText("");
        txtcin.setText("");
        txttelephone.setText("");
        txtnom.setText("");
        txtemail.setText("");
        imageView.setImageResource(R.drawable.user_icon);
        textView.setText("Selectionner une image");
        imageView2.setImageResource(R.drawable.cardid);
        textView2.setText("Selectionner une image");
        imageView3.setImageResource(R.drawable.passport);
        textView3.setText("Selectionner une image");
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecetionner une image"), PICK_IMAGE_REQUEST);
    }
    private void showFileChooserCIN() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecetionner une image"), PICK_CIN_REQUEST);
    }
    private void showFileChooserPASS() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecetionner une image"), PICK_PASS_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature);
        //ferme cette page si les inscriptions sont fermées
        getStatus();
        Intent intent = getIntent();
        typeCandidature= intent.getExtras().getString("typeCand");
         btnTerm = findViewById(R.id.btnterm);
        if (typeCandidature.equals("g"))
        {
            btnTerm.setVisibility(View.VISIBLE);
        }
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
                        CandidatureActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        textView =  findViewById(R.id.pdptextview);
        imageView2 =  findViewById(R.id.cinImageView);
        textView2 =  findViewById(R.id.cinptextview);
        imageView3 =  findViewById(R.id.passImageView);
        textView3 =  findViewById(R.id.passtextview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.btnPdp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CandidatureActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CandidatureActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(CandidatureActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    showFileChooser();
                }
            }
        });
        findViewById(R.id.btncin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CandidatureActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CandidatureActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {
                    } else {
                        ActivityCompat.requestPermissions(CandidatureActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    showFileChooserCIN();
                }
            }
        });
        findViewById(R.id.btnpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CandidatureActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CandidatureActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {
                    } else {
                        ActivityCompat.requestPermissions(CandidatureActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    showFileChooserPASS();
                }
            }
        });
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
                        CandidatureActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }else  if (requestCode == PICK_CIN_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

                    textView2.setText("Fichier Selectionné");
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    imageView2.setImageBitmap(bitmap2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        CandidatureActivity.this,"Pas d'image séléctionnée",
                        Toast.LENGTH_LONG).show();
            }
        }
        else  if (requestCode == PICK_PASS_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

                    textView3.setText("Fichier Selectionné");
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    imageView3.setImageBitmap(bitmap3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        CandidatureActivity.this,"Pas d'image séléctionnée",
                        Toast.LENGTH_LONG).show();
            }
        }

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

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIServices.URL_BASE+APIServices.URL_CANDIDATURE+APIServices.URL_CANDIDAT_IMG_METHOD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            if (response.statusCode == 200) {
                                Toast.makeText(getApplicationContext(), "Candidat Ajouté", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Erreur Du serveur", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Erreur du serveur", Toast.LENGTH_LONG).show();
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
    private void uploadBitmap2(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIServices.URL_BASE+APIServices.URL_CANDIDATURE+APIServices.URL_CANDIDAT_IMG_METHOD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            if (response.statusCode == 200) {
                                Toast.makeText(getApplicationContext(), "Candidat Ajouté", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Erreur Du serveur", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Erreur du serveur", Toast.LENGTH_LONG).show();
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("file", new DataPart(documents , getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
  /*      if(typeCandidature.equals("i"))
        {
            finish();
        }
        else
        {
            clearFields();
        }*/
    }
    private void uploadBitmap3(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIServices.URL_BASE+APIServices.URL_CANDIDATURE+APIServices.URL_CANDIDAT_IMG_METHOD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            if (response.statusCode == 200) {
                                Toast.makeText(getApplicationContext(), "Candidat Ajouté", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Erreur Du serveur", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Erreur du serveur", Toast.LENGTH_LONG).show();
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("file", new DataPart( "passport" + documents  , getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
        if(typeCandidature.equals("i"))
        {
            finish();
        }
        else
        {
            clearFields();
        }
    }

    public void sbmitClick(View view) {
        AttachCandidature();

    }
    public  void GenerateQRCode(String code, final String username, final String dest)
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        final String cd = code;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(code,BarcodeFormat.QR_CODE,200,200);
            final Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);
            for (int x =0;x<200;x++){
                for(int y =0;y<200;y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK:Color.WHITE);
                }
            }
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    SendEmailService.getInstance(getApplicationContext()).SendEmail(dest,"Votre CodeQR de connexion","voici votre code",bitmap,username,cd);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void SendCandidat(Long id)
    {
        nom = txtnom.getText().toString().trim();
        prenom = txtprenom.getText().toString().trim();
        pdp = System.currentTimeMillis() + ".png";
        datenaiss = txtdatenaiss.getText().toString().trim();
        cin = txtcin.getText().toString().trim();
        code = cin + System.currentTimeMillis();
        sexe = genrespin.getSelectedItem().toString();
        documents = "doc" + System.currentTimeMillis() + ".png";
        telephone = txttelephone.getText().toString().trim();
        nbcandidature = "1";
        email = txtemail.getText().toString().trim();
        JSONObject js = new JSONObject();
        try {
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
            if (isFirstSent)
            {
                js.put("idCandidature", sharedId);
            }
            else
            {
                js.put("idCandidature", id);
            }
            js.put("email",email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, APIServices.URL_BASE+APIServices.URL_CANDIDATURE, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GenerateQRCode(response.getString("code"),response.getString("nom"),response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),"Erreur du serveur",Toast.LENGTH_LONG);
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
        uploadBitmap2(bitmap2);
        uploadBitmap3(bitmap3);
    }
    public void AttachCandidature()
    {
        JSONObject js = new JSONObject();
        try {
            js.put("année", Calendar.getInstance().get(Calendar.YEAR));
            js.put("type",typeCandidature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, APIServices.URL_BASE+APIServices.URL_CANDIDATURE1, js,
                new Response.Listener<JSONObject>() {
                    Long id;
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(DEBUGTAG, response.toString() + " i am queen");
                        try {
                            id = response.getLong("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(!isFirstSent)
                        {
                            SendCandidat(id);
                            isFirstSent = true;
                            sharedId = id;
                        }
                        else {
                            SendCandidat(id);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(DEBUGTAG, "Error: " + error.getMessage());
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

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public int getStatus() {

        StringRequest req = new StringRequest(Request.Method.GET, APIServices.URL_BASE + APIServices.URL_ETAT + 1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;

                try {
                    json = new JSONObject(response);
                    if(json.has("error"))
                        Toast.makeText(CandidatureActivity.this,json.getString("error"),Toast.LENGTH_LONG).show();
                    else
                    stat =  json.getInt("etatinstcription");
                    if(stat == 0) {
                        Toast.makeText(CandidatureActivity.this,"Les Inscriptions sont férmées",Toast.LENGTH_LONG);
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
                    Toast.makeText(CandidatureActivity.this,"Problem de connection avec le serveur", Toast.LENGTH_LONG);
                }catch (Exception e)
                {
                    Toast.makeText(CandidatureActivity.this,"Une erreur s'est produite", Toast.LENGTH_LONG);
                }
            }
        });
        HabousVolley.getInstance(this.getApplicationContext()).addToRequestQueue(req);
        return stat;
    }
    public void term(View view)
    {
        finish();
    }
}
