package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.services.DataStorage;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/rooms")
public class RoomResource {

    /**
     * UNIFIED Task 2.1 & 3.1: Retrieve all rooms with optional filtering
     * This handles both GET /rooms AND GET /rooms?minCapacity=X
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms(@QueryParam("minCapacity") Integer minCapacity) {
        List<Room> roomList = new ArrayList<>(DataStorage.getRooms().values());

        // If the user provided a minCapacity, filter the list
        if (minCapacity != null) {
            roomList = roomList.stream()
                .filter(r -> r.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
        }

        return Response.ok(roomList).build();
    }

    /**
     * Task 1.2: Retrieve a specific room by ID
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
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"Room ID is required\"}")
                           .build();
        }

        if (room.getCapacity() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"Capacity cannot be negative\"}")
                           .build();
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStorage.getRooms().put(room.getId(), room);
        
        return Response.status(Response.Status.CREATED)
                       .entity(room)
                       .build();
    }
    
    /**
     * Task 2.4: Delete a room
     */
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStorage.getRooms().get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Room not found\"}")
                           .build();
        }

        if (!room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT) 
                           .entity("{\"error\": \"Cannot delete room. Remove sensors first.\"}")
                           .build();
        }

        DataStorage.getRooms().remove(roomId);
        return Response.noContent().build(); 
    }
}