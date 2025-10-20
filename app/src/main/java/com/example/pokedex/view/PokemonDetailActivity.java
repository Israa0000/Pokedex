package com.example.pokedex.view;

import android.os.Bundle;
import android.widget.Button; // ¡NUEVO! Importa la clase Button
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;

public class PokemonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        int pokemonId = getIntent().getIntExtra("POKEMON_ID", 0);
        String pokemonName = getIntent().getStringExtra("POKEMON_NAME");
        String pokemonSpriteUrl = getIntent().getStringExtra("POKEMON_SPRITE_URL");

        ImageView spriteImageView = findViewById(R.id.imagenPokemon);
        TextView dataTextView = findViewById(R.id.eDatosPokemon);
        Button btnBack = findViewById(R.id.btnBackToList);

        if(pokemonName.isEmpty() || pokemonSpriteUrl.isEmpty()){
            pokemonName = "Unknown";
        }

        String pokemonData = String.format(
                "ID: #%d\nName: %s",
                pokemonId,
                pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1)
        );
        dataTextView.setText(pokemonData);

        Glide.with(this)
                .load(pokemonSpriteUrl)
                .into(spriteImageView);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
