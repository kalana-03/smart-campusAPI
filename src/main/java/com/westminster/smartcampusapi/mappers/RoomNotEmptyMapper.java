package com.westminster.smartcampusapi.mappers;

import com.westminster.smartcampusapi.exceptions.RoomNotEmptyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider // This tells Jersey to use this class to handle errors
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        return Response.status(Response.Status.CONFLICT) // 409
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\": \"" + exception.getMessage() + "\"}")
                .build();
    }
}