package com.zybooks.matt_tranchina_project_two_weight_app;

import java.util.Date;

public class WeightEntry {
    private int id;
    private Date date;
    private float weight;

    //Constructors
    public WeightEntry(Date date, float weight) {
        this.date = date;
        this.weight = weight;
    }

    public WeightEntry(int id, Date date, float weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }

    //Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
}
