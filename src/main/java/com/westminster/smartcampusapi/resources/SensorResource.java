package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.exceptions.LinkedResourceNotFoundException;
import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.models.Sensor;
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
            // This triggers the LinkedResourceNotFoundMapper -> 422
            throw new LinkedResourceNotFoundException("Room " + sensor.getRoomId() + " not found.");
        }

        DataStorage.getSensors().put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        
        // FIX: Handle missing sensor first to avoid NullPointerException
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .type(MediaType.APPLICATION_JSON)
                           .entity("{\"error\": \"Sensor not found\"}")
                           .build();
        }

        // Clean up the reference in the Room
        Room room = DataStorage.getRooms().get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }

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