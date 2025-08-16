package com.example.mobileapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kamera);

        // Pobieranie url strony z poprzedniego okna
        String urlStrony = getIntent().getStringExtra("URL_STRONY");

        Button buttonPowrot = findViewById(R.id.buttonPowrotK);
        WebView webViewStronaK = findViewById(R.id.webViewStronaK);
        webViewStronaK.getSettings().setJavaScriptEnabled(true);
        webViewStronaK.setWebViewClient(new WebViewClient());
        webViewStronaK.loadUrl(urlStrony);

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