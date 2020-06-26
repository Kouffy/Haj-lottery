package com.example.habous.Views.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.habous.APISERVICES.APIServices;
import com.example.habous.APISERVICES.HabousVolley;
import com.example.habous.Helpers.ImageSaver;
import com.example.habous.Helpers.SessionManager;
import com.example.habous.R;
import com.example.habous.Views.Candidat.CandidatLoginActivity;
import com.example.habous.Views.User.UserAndAdmin.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeUserActivity extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    Bitmap bimapheader;
    String hd2,hd1,hdimg;
    ImageView headimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        toolbar=findViewById(R.id.admtoolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.navmenu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggle.syncState();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        hdimg = sessionManager.pref.getString("imagepdp","user");
         hd1 = sessionManager.pref.getString("nomcomplet","user");
        hd2 = sessionManager.pref.getString("role","user");
        View headerView = nav.getHeaderView(0);
        headimg = headerView.findViewById(R.id.headerimage);
        TextView headtxt1 = headerView.findViewById(R.id.headeruser);
        TextView headtxt2 = headerView.findViewById(R.id.headeremail);
        getBitmapFromURL(hdimg);
        headtxt1.setText(hd1);
        headtxt2.setText(hd2);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.HomeUs :
                        navToHome();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Profile :
                        navToProfile();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.ListeCandidat:
                        navToListeCandidats();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.NewCandidat :
                        navToCandidatureI();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.NewCandidatg :
                        navToCandidatureG();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Logout :
                        Deconnection();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }
    public void navToListeCandidats() {
        Intent i = new Intent(getApplicationContext(), ListeCandidatsActivity.class);
        startActivity(i);
    }
    public void navToHome() {
        Intent i = new Intent(getApplicationContext(), HomeUserActivity.class);
        startActivity(i);
        finish();
    }
    public void navToCandidatureI() {
        startActivity(new Intent(getApplicationContext(), CandidatureActivity.class).putExtra("typeCand" ,"i"));
    }
    public void navToCandidatureG() {
    startActivity(new Intent(getApplicationContext(), CandidatureActivity.class).putExtra("typeCand" ,"g"));
    }

    public void navToProfile() {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
    }
    public void Deconnection() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.logoutUser();
        finish();
    }
    public  void getBitmapFromURL(final String src) {
        ImageRequest request = new ImageRequest(APIServices.URL_BASE + APIServices.URL_USER +APIServices.URL_GET_UTILISATEUR_IMG_METHOD + src,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            headimg.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(HomeUserActivity.this, "Lutulisateur n'as pas de photo", Toast.LENGTH_LONG);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        HabousVolley.getInstance(this).addToRequestQueue(request);
    }

}
