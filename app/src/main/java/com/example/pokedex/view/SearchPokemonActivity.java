package com.example.pokedex.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.controller.API;
import com.example.pokedex.model.Pokemon;


import java.util.ArrayList;
//implements

public class SearchPokemonActivity extends AppCompatActivity {


    Button btnById, btnByName;
    EditText searchEditText;
    ImageView spriteImageView;
    TextView dataTextView;
    Button btnGoBackToMenu;
    ArrayList<Pokemon> fullPokedex;
    public Pokemon currentPokemon;
    enum SearchMode {
        NONE, BY_ID, BY_NAME
    }
    SearchMode currentSearchMode = SearchMode.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pokemon);

        btnById = findViewById(R.id.btnById);
        btnByName = findViewById(R.id.ByNameButton);
        searchEditText = findViewById(R.id.Search);
        spriteImageView = findViewById(R.id.spritePokemon);
        dataTextView = findViewById(R.id.datosPokemon);
        btnGoBackToMenu = findViewById(R.id.btnGoBackToMenu);

        btnByName.setBackgroundColor(getColor(R.color.green));
        btnByName.setTextColor(getColor(R.color.blue));

        //Ocultar ls elementos al inicio
        searchEditText.setVisibility(View.GONE);
        spriteImageView.setVisibility(View.GONE);
        dataTextView.setVisibility(View.GONE);

        this.fullPokedex = API.getMyPokedex();
        if (this.fullPokedex.isEmpty()) {
            Toast.makeText(this, "Error: La lista de Pokémon no se ha cargado.", Toast.LENGTH_LONG).show();
            btnById.setEnabled(false);
            btnByName.setEnabled(false);
        }
        seePokemonData();

        setupListeners();

    }

    void seePokemonData(){
        spriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(SearchPokemonActivity.this, PokemonDetailActivity.class);
                    intent.putExtra("POKEMON_ID", currentPokemon.getId());
                    intent.putExtra("POKEMON_NAME", currentPokemon.getName());
                    intent.putExtra("POKEMON_SPRITE_URL", currentPokemon.getImageUrl());
                    startActivity(intent);
            }
        });

    }
    private void setupListeners() {
        btnById.setOnClickListener(v -> {
            currentSearchMode = SearchMode.BY_ID;
            prepareSearchUI("Buscar por ID (ej: 25)");
            searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        });

        btnByName.setOnClickListener(v -> {
            currentSearchMode = SearchMode.BY_NAME;
            prepareSearchUI("Buscar por Nombre (ej: pikachu)");
            searchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        btnGoBackToMenu.setOnClickListener(v -> {
            finish();
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                if (query.isEmpty()) {
                    clearResults();
                    return;
                }
                if (currentSearchMode == SearchMode.BY_ID) {
                    searchPokemonById(query);
                } else if (currentSearchMode == SearchMode.BY_NAME) {
                    searchPokemonByName(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    void prepareSearchUI(String hint) {
        searchEditText.setVisibility(View.VISIBLE);
        searchEditText.setText("");
        searchEditText.setHint(hint);
        clearResults();
    }

    void searchPokemonById(String query) {
        try {
            int pokemonId = Integer.parseInt(query);
            for (Pokemon pokemon : fullPokedex) {
                if (pokemon.getId() == pokemonId) {
                    displayPokemon(pokemon);
                    return;
                }
            }
        } catch (NumberFormatException e) {

        }
        clearResults();
    }

    void searchPokemonByName(String query) {
        for (Pokemon pokemon : fullPokedex) {
            if (pokemon.getName().toLowerCase().startsWith(query)) {
                displayPokemon(pokemon);
                return;
            }
        }
        clearResults();
    }

    void displayPokemon(Pokemon pokemon) {
        spriteImageView.setVisibility(View.VISIBLE);
        dataTextView.setVisibility(View.VISIBLE);
        this.currentPokemon= pokemon;

        String pokemonData = String.format(
                "ID: %d\nNombre: %s",
                pokemon.getId(),
                pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1)
        );
        dataTextView.setText(pokemonData);

        Glide.with(this)
                .load(pokemon.getImageUrl())
                .into(spriteImageView);
    }

    void clearResults() {
        spriteImageView.setVisibility(View.GONE);
        dataTextView.setVisibility(View.GONE);
    }
}
