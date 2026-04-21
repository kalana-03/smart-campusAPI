package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.services.DataStorage;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
public class RoomResource {

    /**
     * Task 2.1: Retrieve all rooms
     * GET http://localhost:8080/api/v1/rooms
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStorage.getRooms().values());
        return Response.ok(roomList).build();
    }

    /**
     * Task 1.2: Retrieve a specific room by ID
     * GET http://localhost:8080/api/v1/rooms/{roomId}
     */
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStorage.getRooms().get(roomId);
        
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Room " + roomId + " not found\"}")
                           .build();
        }
        
        return Response.ok(room).build();
    }

    /**
     * Task 2.1: Add a new room
     * POST http://localhost:8080/api/v1/rooms
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room) {
        // 1. Basic ID Validation
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"Room ID is required\"}")
                           .build();
        }

        // 2. Capacity Validation (New for your updated model)
        if (room.getCapacity() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"Capacity cannot be negative\"}")
                           .build();
        }

        // 3. Ensure sensor list isn't null (Safety check)
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStorage.getRooms().put(room.getId(), room);
        
        return Response.status(Response.Status.CREATED)
                       .entity(room)
                       .build();
    }
}