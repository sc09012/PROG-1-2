package com.project.model;

import com.google.gson.Gson;

public class Location {
    private float x;
    private Long y;
    private float z;
    private String name;

    public Location(float x, Long y, float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public static Location fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Location.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
