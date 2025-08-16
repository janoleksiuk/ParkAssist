package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MojeKameryActivity extends AppCompatActivity implements KameraViewInterface{

    List<Parking> parkingi = new ArrayList<Parking>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_moje_kamery);

        // Dodawanie kolejnych parkingów Park+Ride do listy wyświetlanej
        parkingi.add(new Parking("P+R Stokłosy","https://www.wtp.waw.pl/parkingi/parking-pr-metro-stoklosy/","52.15394,21.03757"));
        parkingi.add(new Parking("P+R Metro Ursynów","https://www.wtp.waw.pl/parkingi/parking-pr-metro-ursynow/","52.16259,21.02742"));
        parkingi.add(new Parking("P+R Metro Wilanowska","https://www.wtp.waw.pl/parkingi/parking-pr-metro-wilanowska/","52.18005,21.02476"));
        parkingi.add(new Parking("P+R Połczyńska","https://www.wtp.waw.pl/parkingi/parking-pr-polczynska/","52.22222,21.92240"));
        // Dodanie naszej kamery, już jako kameraP_R
        parkingi.add(new Parking("Parking Wydziału Mechatroniki Politechiniki Warszawskiej","https://api.thingspeak.com/channels/2815765/fields/1.json","52.20309,21.00182"));

        RecyclerView recyclerView = findViewById(R.id.recycleViewKameryMK);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new KameraAdapter(getApplicationContext(), parkingi,this));
        Button buttonPowrot = findViewById(R.id.buttonPowrotMK);

        buttonPowrot.setOnClickListener(v->{
            super.onBackPressed();
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent;
        String UrlStrony = parkingi.get(position).getUrlStrony();
        if (UrlStrony.contains("https://www.wtp.waw.pl/parkingi/"))
            intent = new Intent(this, KameraP_RActivity.class);
        else
            intent = new Intent(this, KameraNaszaActivity.class);

        // Przesyłanie url strony danego parkingu do kolejnego okna
        intent.putExtra("URL_STRONY", parkingi.get(position).getUrlStrony());
        intent.putExtra("POLOZENIE_NA_MAPIE", parkingi.get(position).getPolozenieNaMapie());
        intent.putExtra("NAZWA_PARKINGU", parkingi.get(position).getNazwaParkingu());
        startActivity(intent);
    }
}