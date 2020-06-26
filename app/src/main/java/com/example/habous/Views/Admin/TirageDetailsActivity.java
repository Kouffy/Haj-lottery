package com.example.habous.Views.Admin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.habous.R;


public class TirageDetailsActivity extends AppCompatActivity {
    TextView detailnbrt,detailnbat,detailres,detaillieu,detaildate;
    int postion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tirage_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailnbrt = findViewById(R.id.detailnbrt);
        detailnbat = findViewById(R.id.detailnbat);
        detailres = findViewById(R.id.detailres);
        detaillieu = findViewById(R.id.detaillieu);
        detaildate = findViewById(R.id.detaildate);
        Intent intent = getIntent();
        postion= intent.getExtras().getInt("position");
        detailnbrt.setText(String.valueOf(ListeTirageActivity.tirageAuSorts.get(postion).getNombreRt()));
        detailnbat.setText(String.valueOf(ListeTirageActivity.tirageAuSorts.get(postion).getNombreAt()));
        detailres.setText(ListeTirageActivity.tirageAuSorts.get(postion).getResponsables());
        detaillieu.setText(ListeTirageActivity.tirageAuSorts.get(postion).getLieu());
        detaildate.setText(ListeTirageActivity.tirageAuSorts.get(postion).getDateTirage());
    }
}
