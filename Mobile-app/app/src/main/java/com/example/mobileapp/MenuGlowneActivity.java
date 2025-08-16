package com.example.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuGlowneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_glowne);

        Button buttonMojeKamery = findViewById(R.id.buttonMojeKameryMG);
        Button buttonUstawienia = findViewById(R.id.buttonUstawieniaMG);
        Button buttonWylogujSie = findViewById(R.id.buttonWylogujSieMG);

        buttonMojeKamery.setOnClickListener(v -> {
            Intent intent = new Intent(this, MojeKameryActivity.class);
            startActivity(intent);
        });

        buttonUstawienia.setOnClickListener(v -> {
            Intent intent = new Intent(this,UstawieniaActivity.class);
            startActivity(intent);
            finish();
        });

        buttonWylogujSie.setOnClickListener(v -> {
            // Utworzenie pop-upa proszącego o potwierdzenie operacji wylogowania się.

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.str_potwierdzenieOperacjiTytul);
            builder.setMessage(R.string.str_czyNaPewnoWylogowacSie);

            builder.setPositiveButton(R.string.str_tak, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MenuGlowneActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.str_nie, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Zamkniecie okienka
                }
            });
            builder.show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}