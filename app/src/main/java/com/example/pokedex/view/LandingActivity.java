package com.example.pokedex.view;

import static com.example.pokedex.controller.API.getMyPokedex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.R;
import com.example.pokedex.controller.API;
import com.example.pokedex.model.Pokemon;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    Button btnSearchPokemon;
    Button btnShowAllPokemons;
    ProgressBar loadingProgressBar;
    // Handler para verificar periódicamente el estado de la carga
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable checkDataLoadedRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        btnSearchPokemon = findViewById(R.id.btnSearchPokemon);
        btnShowAllPokemons = findViewById(R.id.btnShowAllPokemons);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        btnShowAllPokemons.setBackgroundColor(getColor(R.color.green));
        btnShowAllPokemons.setTextColor(getColor(R.color.blue));

        loadingProgressBar.setVisibility(View.VISIBLE);

        btnSearchPokemon.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, SearchPokemonActivity.class);
            startActivity(intent);
        });

        btnShowAllPokemons.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, ShowAllPokemonsActivity.class);
            startActivity(intent);
        });

        // carga de datos en segundo plano
        loadPokedexData();
    }

        void loadPokedexData() {
        // Se llama a la API para empezar a descargar lo datso
        API.loadPokedexData(this);

        // Se crea un runable que se ejecutará para comprobar si los datos han cargado.
        checkDataLoadedRunnable = new Runnable() {
            @Override
            public void run() {
                ArrayList<Pokemon> pokedex = getMyPokedex();
                // se comprueba si la lista ya se ha llenado.
                if (pokedex.size() >= 1028) {

                    Log.d("LandingActivity", "Carga de datos completada. Total: " + pokedex.size() + " Pokémon.");

                    // Ocultar la barra de progreso
                    loadingProgressBar.setVisibility(View.GONE);

                    // Habilitar los botones
                    btnSearchPokemon.setEnabled(true);
                    btnShowAllPokemons.setEnabled(true);

                    Toast.makeText(LandingActivity.this, "¡Pokédex lista para usar!", Toast.LENGTH_SHORT).show();

                    // la verificacion ya no es necesaria así que detenemos el handler.
                    handler.removeCallbacks(this);
                } else {
                    // Si los datos aún no están listos se vuelve a ejecutar lacomprobación despues de un segundo

                    handler.postDelayed(this, 1000); //1000ms = 1 segundo
                }
            }
        };

        // enmpezar la primera verificación.
        handler.post(checkDataLoadedRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Es una buena práctica eliminar los callbacks del handler cuando la actividad se destruye
        // para evitar fugas de memoria.
        if (checkDataLoadedRunnable != null) {
            handler.removeCallbacks(checkDataLoadedRunnable);
        }
    }
}
