package com.example.habous.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.habous.Models.Candidat;

import java.util.ArrayList;

public class LocalDB extends SQLiteOpenHelper {
    public static  final  String bdName ="habousAppLocalDB.db";
    String candidatTable  ="CREATE TABLE CANDIDAT (id INTEGER PRIMARY KEY,nom TEXT,prenom TEXT,pdp String,daten TEXT," +
            "code TEXT,sexe TEXT,cin TEXT,documents String,ncandidature INTEGER,tel TEXT,email TEXT,idcand INTEGER)";
    public LocalDB(@Nullable Context context) {
        super(context, bdName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(candidatTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CANDIDAT ");
        onCreate(db);
    }
    public Boolean insertData(Long id,String nom,String prenom,String  pdp,String daten,String code,String sexe,String cin,String  documents,int ncandidature,String  tel,String email,Long idcand){
        SQLiteDatabase sql =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("nom",nom);
        contentValues.put("prenom",prenom);
        contentValues.put("pdp",pdp);
        contentValues.put("daten",daten);
        contentValues.put("code",code);
        contentValues.put("sexe",sexe);
        contentValues.put("cin",cin);
        contentValues.put("documents",documents);
        contentValues.put("ncandidature",ncandidature);
        contentValues.put("tel",tel);
        contentValues.put("email",email);
        contentValues.put("idcand",idcand);
        long result = sql.insert("CANDIDAT",null ,contentValues);
        if(result == -1){
            return  false ;
        }
        else
        {
            return  true;
        }
    }
    public Cursor getLocalCandidat(){
        SQLiteDatabase sql = this.getReadableDatabase();
        Cursor crs = sql.rawQuery("SELECT * FROM CANDIDAT LIMIT 1",null);
        return  crs;
    }
    public void Trnc(Context context)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("CANDIDAT",null,null);
        Toast.makeText(context,"Vos donnes sont supprimé du mobile",Toast.LENGTH_LONG);
    }
}
