package com.westminster.smartcampusapi.models;

import java.util.UUID;

public class Reading {
    private String id;        // UUID
    private double value;    
    private long timestamp;   // Epoch milliseconds

    //defualt constructor 
    public Reading() {
    }

    public Reading(double value) {
        this.id = UUID.randomUUID().toString();
        this.value = value;
        this.timestamp = System.currentTimeMillis(); 
    }
  

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}