package com.example.mobileapp;

import static android.app.UiModeManager.MODE_NIGHT_NO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class UstawieniaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ustawienia);


        Button buttonPowrot = findViewById(R.id.buttonPowrotU);

        // Odczytanie stanów spinnerów
        String folder = "filename"; // Miejsce do przechowywania stanów spinnerów
        SharedPreferences sharedPref = getSharedPreferences(folder,MODE_PRIVATE);
        int iWybranyJezyk = sharedPref.getInt("wybranyJezyk",-1);
        int iWybranyMotyw = sharedPref.getInt("wybranyMotywWyswietlania",-1);

        // Utworzenie listy z trybami wyswietlania aplikacji
        ArrayList<String> listaTrybowWyswietlania = new ArrayList<>();
        listaTrybowWyswietlania.add(getResources().getString(R.string.str_motywJasny));
        listaTrybowWyswietlania.add(getResources().getString(R.string.str_motywCiemny));
        ArrayAdapter<String> adapterMotywWyswietlania = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,listaTrybowWyswietlania);
        adapterMotywWyswietlania.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        Spinner spinnerMotywWyswietlania = findViewById(R.id.spinnerMotywWyswietlaniaU);
        spinnerMotywWyswietlania.setAdapter(adapterMotywWyswietlania);
        if(iWybranyMotyw != -1){
            spinnerMotywWyswietlania.setSelection(iWybranyMotyw);
        }

        // Utworzenie listy z językami
        ArrayList<String> listaJezykow = new ArrayList<>();
        listaJezykow.add(getResources().getString(R.string.str_Angielski));
        listaJezykow.add(getResources().getString(R.string.str_Polski));
        ArrayAdapter<String> adapterJezyki = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,listaJezykow);
        adapterJezyki.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        Spinner spinnerJezyki = findViewById(R.id.spinnerJezykU);
        spinnerJezyki.setAdapter(adapterJezyki);
        if(iWybranyJezyk != -1){
            spinnerJezyki.setSelection(iWybranyJezyk);
        }

        spinnerMotywWyswietlania.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String wybranyTryb = parent.getItemAtPosition(position).toString();
                if(wybranyTryb.equals(getResources().getString(R.string.str_motywJasny)))
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (wybranyTryb.equals(getResources().getString(R.string.str_motywCiemny))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                zapiszStan(spinnerMotywWyswietlania,folder,"wybranyMotywWyswietlania");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerJezyki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String wybranyJezyk = parent.getItemAtPosition(position).toString();
                if(wybranyJezyk.equals(getResources().getString(R.string.str_Polski))) {
                    ustawJezyk("Default");
                }
                else {
                    ustawJezyk("en");
                }

                zapiszStan(spinnerJezyki,folder,"wybranyJezyk");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonPowrot.setOnClickListener(v -> {
            Intent intent = new Intent(this,MenuGlowneActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void ustawJezyk(String jezyk) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(jezyk);
        resources.updateConfiguration(configuration,metrics);
    }
    // Funkcja zapisująca stan spinnera po wyjściu z ekranu ustawień.
    private void zapiszStan(Spinner spinner,String folder,String nazwaZmiennej) {
        int zmienna = spinner.getSelectedItemPosition();
        SharedPreferences sharedPref = getSharedPreferences(folder,0);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt(nazwaZmiennej,zmienna);
        prefEditor.commit();
    }
}