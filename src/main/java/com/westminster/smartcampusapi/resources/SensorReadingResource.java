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

/**
 * Sub-resource for handling readings of a specific sensor.
 * Path: /sensors/{sensorId}/readings
 */
public class SensorReadingResource {
    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reading> getReadings() {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found");
        }
        return sensor.getReadings();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(Reading newReading) {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        
        // Safety check if sensor exists
        if (sensor == null) {
            throw new NotFoundException("Sensor " + sensorId + " not found.");
        }

        // Task 5.3: Check sensor status
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is currently under maintenance.");
        }

        // --- SPEC ALIGNMENT (Task 3.3) ---
        newReading.setId(UUID.randomUUID().toString());      // Required UUID
        newReading.setTimestamp(System.currentTimeMillis()); // Required Epoch Milliseconds
        
        // Update sensor state
        sensor.setCurrentValue(newReading.getValue());
        sensor.getReadings().add(newReading);

        return Response.status(Response.Status.CREATED)
                .entity(newReading)
                .build();
    }
}