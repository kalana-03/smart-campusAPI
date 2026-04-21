package com.westminster.smartcampusapi.models;

import java.util.ArrayList; 
import java.util.List;      

public class Sensor {
    private String id;
    private String type; // e.g., "Temperature"
    private String roomId;
    private double lastReading;
    private List<Reading> readings = new ArrayList<>();


    public Sensor() {}

    public Sensor(String id, String type, String roomId) {
        this.id = id;
        this.type = type;
        this.roomId = roomId;
    }
    
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public double getLastReading() { return lastReading; }
    public void setLastReading(double lastReading) { this.lastReading = lastReading; }
    public List<Reading> getReadings() { return readings; }
    public void setReadings(List<Reading> readings) { this.readings = readings; }

}