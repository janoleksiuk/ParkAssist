package com.example.mobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class KameraNaszaActivity extends AppCompatActivity {
    ThingSpeakReader thingSpeakReader = new ThingSpeakReader();
    String obecnaIloscMiejsc = "brak";
    TextView textViewIloscMiejsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kamera_nasza);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlStrony = getIntent().getStringExtra("URL_STRONY");
        String polozenieNaMapie = getIntent().getStringExtra("POLOZENIE_NA_MAPIE");
        String nazwaParkingu = getIntent().getStringExtra("NAZWA_PARKINGU");

        Button buttonPowrot = findViewById(R.id.buttonPowrotKN);
        Button buttonPokazNaMapie = findViewById(R.id.buttonPokazNaMapieKN);
        textViewIloscMiejsc = findViewById(R.id.textViewLiczbaMiejscKN);

        obecnaIloscMiejsc = thingSpeakReader.readFromThingSpeak();
        textViewIloscMiejsc.setText(obecnaIloscMiejsc);

        buttonPowrot.setOnClickListener(v->{
            super.onBackPressed();
            finish();
        });

        buttonPokazNaMapie.setOnClickListener(v -> {
            String urlMapy = "http://maps.google.com/maps?q="+ polozenieNaMapie +"("+ nazwaParkingu + ")&iwloc=A&hl=es";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlMapy));
            Intent chooser = Intent.createChooser(intent,"Launch Map");
            startActivity(chooser);
        });
        countDownTimer.start();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    CountDownTimer countDownTimer = new CountDownTimer(60000,10000) {
        @Override
        public void onTick(long millisUntilFinished) {
            obecnaIloscMiejsc = thingSpeakReader.readFromThingSpeak();
            textViewIloscMiejsc.setText(obecnaIloscMiejsc);
        }

        @Override
        public void onFinish() {

            countDownTimer.start();
        }
    }.start();

}