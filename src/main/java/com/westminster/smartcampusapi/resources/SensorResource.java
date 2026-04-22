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

//Part 3.1 - Sensor Resource & Integrity
// manages the sensors collection
@Path("/sensors")
public class SensorResource {
    
    // Part 3.1 - POST /api/v1/sensors
    // register a new sesnor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {
        Room room = DataStorage.getRooms().get(sensor.getRoomId());
        
        if (room == null) {
            // if room is null triggers LinkedResourceNotFoundException (422)
            throw new LinkedResourceNotFoundException("Room " + sensor.getRoomId() + " not found.");
        }

        DataStorage.getSensors().put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    //Part 4.1 - Sub-Resource Locator Pattern
    // past the request to SensorReadingResource
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    // Part 3.1 - DELETE /api/v1/sensors/{sensorId}
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStorage.getSensors().get(sensorId);
        
        // handle missing sensor first to avoid NullPointerException
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .type(MediaType.APPLICATION_JSON)
                           .entity("{\"error\": \"Sensor not found\"}")
                           .build();
        }

        // clean up the reference in the room
        Room room = DataStorage.getRooms().get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }

        DataStorage.getSensors().remove(sensorId);
        return Response.noContent().build();
    }

    // Part 3.2 -  Filtered Retrieval & Search
    // GET /api/v1/sensors
    // return all the sesnors 
    // GET /api/v1/sensors?type=CO2
    // filter by type if wanted
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(DataStorage.getSensors().values());
        // validate the type
        if (type != null && !type.isEmpty()) {
            sensorList = sensorList.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type)) // filter by type
                .collect(java.util.stream.Collectors.toList()); // add them to a collection
        }
        return Response.ok(sensorList).build();
    }
}