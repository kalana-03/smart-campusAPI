package com.westminster.smartcampusapi.models;

import java.time.LocalDateTime;

public class Reading {
    private double value;
    private String timestamp;

    public Reading() {}

    public Reading(double value) {
        this.value = value;
        this.timestamp = LocalDateTime.now().toString();
    }

    // Getters and Setters
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}