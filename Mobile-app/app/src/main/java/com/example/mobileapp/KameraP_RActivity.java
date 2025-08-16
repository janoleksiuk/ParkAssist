package com.example.mobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KameraP_RActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kamera_p_r);

        // Pobieranie url strony z poprzedniego okna
        String urlStrony = getIntent().getStringExtra("URL_STRONY");
        String polozenieNaMapie = getIntent().getStringExtra("POLOZENIE_NA_MAPIE");
        String nazwaParkingu = getIntent().getStringExtra("NAZWA_PARKINGU");

        Button buttonPowrot = findViewById(R.id.buttonPowrotKP_R);
        Button buttonPokazNaMapie = findViewById(R.id.buttonPokazNaMapieKP_R);

        WebView webViewStronaKP_R = findViewById(R.id.webViewStronaKP_R);
        webViewStronaKP_R.getSettings().setJavaScriptEnabled(true);
        webViewStronaKP_R.setWebViewClient(new WebViewClient());
        webViewStronaKP_R.loadUrl(urlStrony);

        buttonPokazNaMapie.setOnClickListener(v->{
            String urlMapy = "http://maps.google.com/maps?q="+ polozenieNaMapie +"("+ nazwaParkingu + ")&iwloc=A&hl=es";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlMapy));
            Intent chooser = Intent.createChooser(intent,"Launch Map");
            startActivity(chooser);
        });

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
}