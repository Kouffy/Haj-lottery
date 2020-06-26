package com.example.habous.Helpers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.habous.LoginActivity;
import com.example.habous.Views.Candidat.CandidatLoginActivity;

public class SessionManager {
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MYSESSION";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String SESSION_TYPE = "type";
    public static final String SESSION_TOKEN= "token";
    public static final String NOM_COMPLET= "nomcomplet";
    public static final String IMAGE_PDP= "imagepdp";
    public static final String ROLE= "role";
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(Long Id,String type,String token,String nom,String img,String role){
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(KEY_ID, Id);
        editor.putString(SESSION_TYPE,type);
        editor.putString(SESSION_TOKEN,token);
        editor.putString(NOM_COMPLET,nom);
        editor.putString(IMAGE_PDP,img);
        editor.putString(ROLE,role);
        editor.commit();
    }
    public  boolean isLoggedIn()
    {
        return   pref.getBoolean(IS_LOGIN,false) == true ? true : false;
    }
    public void checkSession(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public void logoutCandidat(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, CandidatLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public String getType(){
        return pref.getString(SESSION_TYPE,"Erreur");
    }

}