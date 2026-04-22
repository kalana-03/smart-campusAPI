package com.westminster.smartcampusapi.services;

import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.models.Sensor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }
}