package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.models.Sensor;
import com.westminster.smartcampusapi.models.Reading; 
import com.westminster.smartcampusapi.services.DataStorage;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
}