package com.project.model;

import com.google.gson.Gson;

public class Coordinates {
    private Integer x;
    private double y;

    public Coordinates(Integer x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinates fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Coordinates.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
