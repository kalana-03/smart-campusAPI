package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.exceptions.SensorUnavailableException;
import com.westminster.smartcampusapi.models.Reading;
import com.westminster.smartcampusapi.models.Sensor;
import com.westminster.smartcampusapi.services.DataStorage;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

// Part 4.1 - The Sub-Resource Locator Pattern
// managing the reading history of a specific sensor
public class SensorReadingResource {
    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }
    
    // Part 4.2 - Historical Data Management
    // retrieve full list of reading recored for the sensor
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reading> getReadings() {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found"); //404
        }
        return sensor.getReadings();
    }
    
    // Part 4.2 - Historical Data Management
    // add a new rreading to sensor history
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(Reading newReading) {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        
        // check if sensor exists
        if (sensor == null) {
            throw new NotFoundException("Sensor " + sensorId + " not found.");
        }

        // Task 5.3: block reading if the sensor under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is currently under maintenance.");
        }

        // asssign UUID and timestamp
        newReading.setId(UUID.randomUUID().toString());    
        newReading.setTimestamp(System.currentTimeMillis()); 
        
        // update the sensor currentvalue
        sensor.setCurrentValue(newReading.getValue());
        sensor.getReadings().add(newReading);

        return Response.status(Response.Status.CREATED)
                .entity(newReading)
                .build();
    }
}