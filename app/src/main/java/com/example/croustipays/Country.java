package com.example.croustipays;

import androidx.annotation.Nullable;

import java.util.List;

public class Country {
    private Name name;
    private int population;
    private List<Double> latlng;

    private Flags flags;

    public String getName(){
        return this.name.getCommon();
    }
    public void setName(Name value){
        name = value;
    }

    public int getPopulation(){
        return population;
    }
    public void setPopulation(int value){
        population = value;
    }

    public List<Double> getLatlng(){
        return latlng;
    }
    public void setatlng(List<Double> value){
        latlng = value;
    }

    public Flags getFlags(){
        return flags;
    }
    public void setFlags(Flags value){
        flags = value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj instanceof Country){
            Country ptr = (Country) obj;
            return ptr.getName().equals(this.name.getCommon());
        }

        return false;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name=" + name +
                ", population=" + population +
                ", flags=" + flags +
                '}';
    }
}
