package com.westminster.smartcampusapi.models;

import java.util.ArrayList; 
import java.util.List;      

public class Sensor {
    private String id;
    private String type; 
    private String roomId;
    private double currentValue;
    private String status;
    private List<Reading> readings = new ArrayList<>();


    public Sensor() {
        this.readings = new ArrayList<>();
        this.status = "ACTIVE"; // defualt status
    }

    public Sensor(String id, String type, String roomId) {
        this.id = id;
        this.type = type;
        this.roomId = roomId;
        this.readings = new ArrayList<>();
        this.status = "ACTIVE";
        this.currentValue = 0.0;
    }
    
    
    // Getters and Setters
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }
    
    public String getType() { 
        return type; 
    }
    public void setType(String type) { 
        this.type = type; 
    }
    
    public String getRoomId() { 
        return roomId; 
    }
    public void setRoomId(String roomId) { 
        this.roomId = roomId; 
    }
    
    public double getCurrentValue() { 
        return currentValue; 
    }
    public void setCurrentValue(double currentValue) { 
        this.currentValue = currentValue; 
    }
    
    public List<Reading> getReadings() { 
        return readings; 
    }
    public void setReadings(List<Reading> readings) { 
        this.readings = readings; 
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}