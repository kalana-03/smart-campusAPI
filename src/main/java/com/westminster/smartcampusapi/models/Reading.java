package com.westminster.smartcampusapi.models;

import java.util.UUID;

public class Reading {
    private String id;        // Required: UUID
    private double value;     // Required
    private long timestamp;   // Required: Epoch Milliseconds

    public Reading() {
        // Default constructor for JSON deserialization
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