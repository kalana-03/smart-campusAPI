package com.westminster.smartcampusapi.exceptions;

//Part 5.1 - Resource Conflict(409)
//This will be thrown when if users try to delete a room that still has a sensor asigned
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}