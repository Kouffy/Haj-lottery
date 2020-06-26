package com.example.habous.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habous.R;

import java.util.List;

public class TirageAdapter extends ArrayAdapter<TirageAuSort> {
        Context context ;
        List<TirageAuSort> listTrage ;
        public TirageAdapter(@NonNull Context context, List<TirageAuSort> listTirage) {
            super(context, R.layout.custom_item_tirage,listTirage);
            this.context = context ;
            this.listTrage = listTirage;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_tirage,null,true);
            TextView costumanne = view.findViewById(R.id.costumdate);
            costumanne.setText("Tirage du : "+listTrage.get(position).getDateTirage());
            return view;
        }

}
