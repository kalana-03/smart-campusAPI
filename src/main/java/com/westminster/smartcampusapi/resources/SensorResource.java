package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.models.Sensor;
import com.westminster.smartcampusapi.models.Reading; 
import com.westminster.smartcampusapi.services.DataStorage;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {
        Room room = DataStorage.getRooms().get(sensor.getRoomId());
        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"Room " + sensor.getRoomId() + " does not exist\"}")
                           .build();
        }

        DataStorage.getSensors().put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @PUT
    @Path("/{sensorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSensorReading(@PathParam("sensorId") String sensorId, Reading newReading) {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Sensor not found\"}")
                           .build();
        }

        // 2. Logic Check: Ensure the incoming value isn't empty/null if needed
        sensor.setLastReading(newReading.getValue());
        
        newReading.setTimestamp(java.time.LocalDateTime.now().toString());
        sensor.getReadings().add(newReading);

        return Response.ok(sensor).build();
    }
    
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStorage.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 1. Remove from the Room's sensor list first
        Room room = DataStorage.getRooms().get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }

        // 2. Remove from global storage
        DataStorage.getSensors().remove(sensorId);

        return Response.noContent().build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(DataStorage.getSensors().values());

        if (type != null && !type.isEmpty()) {
            sensorList = sensorList.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(java.util.stream.Collectors.toList());
        }

        return Response.ok(sensorList).build();
    }
}