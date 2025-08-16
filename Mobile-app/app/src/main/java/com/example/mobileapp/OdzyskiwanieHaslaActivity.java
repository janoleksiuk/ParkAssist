package com.example.mobileapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OdzyskiwanieHaslaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_odzyskiwanie_hasla);

        Button buttonPowrot = findViewById(R.id.buttonPowrotOH);
        Button buttonPrzeslijEmail = findViewById(R.id.buttonPrzeslijWiadomoscOH);
        TextView komunikat = findViewById(R.id.textViewKomunikatOH);
        TextView editViewEmail = findViewById(R.id.editTextEmailOH);

        buttonPowrot.setOnClickListener(v -> {
            super.onBackPressed();
            finish();
        });

        buttonPrzeslijEmail.setOnClickListener(v -> {
            String sEmail = editViewEmail.getText().toString().trim();
            // Sprawdzanie czy pole tekstowe z mailem nie jest puste
            if(!TextUtils.isEmpty(sEmail)) {

                wyslijWiadomosc();
                komunikat.setText(R.string.str_emailPrzeslany);
            }
            else {
                komunikat.setText(R.string.str_niepoprawnyEmail);
            }
            // Schowanie klawiatury po przeslaniu wiadomosci
            editViewEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void wyslijWiadomosc()
    {
        // Kod przesyłający wiadomość e-mail na wskazany adres
    }
}