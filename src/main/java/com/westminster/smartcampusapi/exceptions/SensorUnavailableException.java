package com.westminster.smartcampusapi.exceptions;

//Part 5.3 - State Constraint (403 Forbidden)
//If the sensor status is MAINTENANCE it will not take any POST request
public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String message) {
        super(message);
    }
}