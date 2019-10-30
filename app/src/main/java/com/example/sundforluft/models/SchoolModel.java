package com.example.sundforluft.models;

import android.content.Context;

import com.example.sundforluft.R;

public class SchoolModel {
    private String name;
    private int currentAir;
    private final Context context;

    public SchoolModel(Context context, String name, int currentAir) {
        this.context = context;
        this.name = name;
        this.currentAir = currentAir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentAir() {
        return currentAir;
    }

    public void setCurrentAir(int currentAir) {
        this.currentAir = currentAir;
    }

    public String getAirQualityString() {
        final int BAD_AIR_QUALITY = 10;
        final int MEDIUM_AIR_QUALITY = 20;
        final int GOOD_AIR_QUALITY = 30;

        if (currentAir < BAD_AIR_QUALITY) { return context.getString(R.string.bad_air_quality); }
        if (currentAir < MEDIUM_AIR_QUALITY) { return context.getString(R.string.medium_air_quality); }
        if (currentAir < GOOD_AIR_QUALITY) { return context.getString(R.string.good_air_quality); }
        return context.getString(R.string.best_air_quality);
    }



}