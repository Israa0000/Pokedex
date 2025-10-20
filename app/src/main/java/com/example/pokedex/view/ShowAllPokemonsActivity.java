package com.example.pokedex.view;

import static com.example.pokedex.controller.API.getMyPokedex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.controller.API;
import com.example.pokedex.model.Pokemon;

import java.util.ArrayList;
import java.util.Collections;

public class ShowAllPokemonsActivity extends AppCompatActivity {

    static int POKEMON_POR_PAGE = 50;
    LinearLayout pokemonContainer;
    Button btnLoadMore;
    ScrollView scrollView;
    Button btnBack;

    //Dtaos
    ArrayList<Pokemon> fullPokedex;
    int currentlyDisplayedPokemons = 0;// cuanta los pokemons q se muestran


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_pokemons);

        pokemonContainer = findViewById(R.id.pokemon_container);
        btnLoadMore = findViewById(R.id.btnLoadMore);
        scrollView = findViewById(R.id.scrollView);
        btnBack = findViewById(R.id.btnBackToMenuFromList);

        setupData();
        setupListeners();

        displayNextPage();
    }

    void setupData() {
        fullPokedex = getMyPokedex();
        Collections.sort(fullPokedex, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
    }

    void setupListeners() {
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnLoadMore.setOnClickListener(v -> {
            displayNextPage();
        });
    }

    void displayNextPage(){
        if (fullPokedex.isEmpty()){
            btnLoadMore.setVisibility(View.GONE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        int limit = Math.min(currentlyDisplayedPokemons + POKEMON_POR_PAGE, fullPokedex.size());

        for (int i = currentlyDisplayedPokemons; i < limit; i++) {
            Pokemon pokemon = fullPokedex.get(i);

            View itemView = inflater.inflate(R.layout.pokemon_list_item, pokemonContainer, false);

            ImageView pokemonSprite = itemView.findViewById(R.id.pokemon_sprite);
            TextView pokemonName = itemView.findViewById(R.id.pokemon_name);

            String formattedName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
            //Primera en mayuscula
            String displayName = String.format("#%d %s", pokemon.getId(), formattedName);
            //# es un caracter, %d es un numero, %s es un string
            pokemonName.setText(displayName);

            Glide.with(this).load(pokemon.getImageUrl()).into(pokemonSprite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowAllPokemonsActivity.this, PokemonDetailActivity.class);

                    intent.putExtra("POKEMON_ID", pokemon.getId());
                    intent.putExtra("POKEMON_NAME", pokemon.getName());
                    intent.putExtra("POKEMON_SPRITE_URL", pokemon.getImageUrl());

                    startActivity(intent);
                }
            });

            pokemonContainer.addView(itemView);
        }

        currentlyDisplayedPokemons = limit;

        if (currentlyDisplayedPokemons >= fullPokedex.size()){
            btnLoadMore.setVisibility(View.GONE);
        } else {
            btnLoadMore.setVisibility(View.VISIBLE);
        }
    }
}