package com.westminster.smartcampusapi.resources;

import com.westminster.smartcampusapi.models.Room;
import com.westminster.smartcampusapi.services.DataStorage;
import com.westminster.smartcampusapi.exceptions.RoomNotEmptyException;
import java.net.URI;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Part 2.1 - Room Resource Implementation
//full CRUD operations for the room entities
@Path("/rooms")
public class RoomResource {

    // Part 2.1 - GET /api/v1/rooms
    // return list of all rooms
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms(@QueryParam("minCapacity") Integer minCapacity) {
        List<Room> roomList = new ArrayList<>(DataStorage.getRooms().values());

        // If the user provided a minCapacity, filter the list
        if (minCapacity != null) {
            roomList = roomList.stream()
                .filter(r -> r.getCapacity() >= minCapacity) //checking r capacity is <= mincapacity
                .collect(Collectors.toList()); //adding them to a list
        }

        return Response.ok(roomList).build();
    }

    // Part 2.1 - GET /api/v1/rooms/{roomId}
    // return 404 not found if the room not exist
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

    //Part 2.1 - POST /api/v1/rooms
    // create new room 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room) {
        // validating the ID
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
        
         URI location = URI.create("/api/v1/rooms/" + room.getId());
        return Response.created(location).entity(room).build();
    }
    
    // Part 2.2 - DELETE /api/v1/rooms/{roomId}
    // Part 5.1 - Room deletion safety logic(409 conflict)
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStorage.getRooms().get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                       .type(MediaType.APPLICATION_JSON)
                       .entity("{\"error\": \"Resource not found\"}")
                       .build();
        }
        
        if (!room.getSensorIds().isEmpty()) {
            // automatically trigger the RoomNotEmptyMapper 
            throw new RoomNotEmptyException("Cannot delete room " + roomId + ". It still contains active sensors.");
        }

        DataStorage.getRooms().remove(roomId);
        return Response.noContent().build(); 
    }
}