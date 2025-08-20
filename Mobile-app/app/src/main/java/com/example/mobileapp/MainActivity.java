package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button buttonZalogujSie = findViewById(R.id.buttonZalogujSie);
        Button buttonZarejestrujSie = findViewById(R.id.buttonZarejestrujSie);
        Button buttonZapomnialemHasla = findViewById(R.id.buttonZapomnialemHasla);

        TextView editViewLogin = findViewById(R.id.editTextLogin);
        TextView editViewHaslo = findViewById(R.id.editTextHaslo);
        TextView textViewKomunikat = findViewById(R.id.textViewKomunikat);

        buttonZalogujSie.setOnClickListener(v -> {
            // Sprawdzanie czy dane logowania sa poprawne
            String sLogin = editViewLogin.getText().toString().trim();
            String sHaslo = editViewHaslo.getText().toString().trim();

            if (!sLogin.equals("JO") || !sHaslo.equals("1234")){
                textViewKomunikat.setText(R.string.str_niepoprawneDaneLogowania);
            }
            else {
                textViewKomunikat.setText("");
                editViewLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);
                editViewHaslo.onEditorAction(EditorInfo.IME_ACTION_DONE);
                Intent intent = new Intent(this,MenuGlowneActivity.class);
                startActivity(intent);
                finish();
            }

        });

        buttonZarejestrujSie.setOnClickListener(v -> {
            Intent intent = new Intent(this,RejestracjaActivity.class);
            startActivity(intent);
        });

        buttonZapomnialemHasla.setOnClickListener(v -> {
            Intent intent = new Intent(this,OdzyskiwanieHaslaActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}