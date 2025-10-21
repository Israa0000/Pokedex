package com.example.pokedex.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokedex.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class API {

    static ArrayList<Pokemon> myPokedex = new ArrayList<>();
    static String TAG = "API_Pokedex";

    public static void loadPokedexData(Context ct) {
        // limpiamos la lista para evitar duplicados si se llama mucjas veces.
        if (myPokedex.isEmpty()) {
            obtainAllPokemonURLs(ct);
        }
    }

    static void obtainAllPokemonURLs(Context ct) {
        RequestQueue queue = Volley.newRequestQueue(ct);
        String url = "https://pokeapi.co/api/v2/pokemon?limit=1028&offset=0";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response); // la respuesta se guardara como un obejto JSON(results)
                        JSONArray results = jObj.getJSONArray("results"); //se guarda la lista de resultados en un array
                        ArrayList<String> urlList = new ArrayList<>(); //creamos una arrayList en las que se guardaran las urls de los pokemon
                        for (int i = 0; i < results.length(); i++) {
                            urlList.add(results.getJSONObject(i).getString("url"));//el bucle es para que se queden todas las urls de los pokemon en la lista
                        }
                        gatherAllPokemonInfo(ct, urlList); //le pasamos la lista para que coja la informacion de cada pokemon
                    } catch (JSONException e) {
                        Log.e(TAG, "Error de JSON en obtainAllPokemonURLs", e);
                    }
                },
                error -> Log.e(TAG, "Error de Volley en obtainAllPokemonURLs", error)
        );

        queue.add(stringRequest);
    }

    static void gatherAllPokemonInfo(Context ct, ArrayList<String> urlList) {
        RequestQueue queue = Volley.newRequestQueue(ct);

        for (String pokemonUrl : urlList) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, pokemonUrl,
                    response -> {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            // Añadimos el nuevo Pokémon a la lista
                            myPokedex.add(new Pokemon(
                                    jObj.getString("name"),
                                    jObj.getJSONObject("sprites").getString("front_default"),
                                    jObj.getInt("id")
                            ));
                        } catch (JSONException e) {
                            Log.e(TAG, "Error en gatherAllPokemonInfo para la URL: " + pokemonUrl, e);
                        }
                    },
                    error -> Log.e(TAG, "Error de Volley en gatherAllPokemonInfo para la URL: " + pokemonUrl, error)
            );
            queue.add(stringRequest);
        }
    }

 //se devuelve la lsita con pokemons (la clase pokemon es la que contiene los datos)
    public static ArrayList<Pokemon> getMyPokedex() {
        return myPokedex;
    }
}
