package com.example.pokedex.model;

public class Pokemon {
    private int id;
    private String name;
    private String imageUrl;


    public Pokemon(String name, String imageUrl, int id) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }

    public String getImageUrl(){
        return imageUrl;
    }
}
