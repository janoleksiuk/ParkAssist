package com.example.mobileapp;

import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacjanatelefon.R;

public class KameraHolder extends RecyclerView.ViewHolder {

    TextView nazwaKamery, polozenieParkingu;
    public KameraHolder(@NonNull View itemView, KameraViewInterface kameraViewInterface) {
        super(itemView);
        nazwaKamery = itemView.findViewById(R.id.nazwaParkinguKNL);
        polozenieParkingu = itemView.findViewById(R.id.polozenieParkinguKNL);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kameraViewInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        kameraViewInterface.onItemClick(pos);
                    }
                }
            }
        });
    }
}