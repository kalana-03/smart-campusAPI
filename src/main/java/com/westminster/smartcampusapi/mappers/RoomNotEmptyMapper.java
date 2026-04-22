package com.westminster.smartcampusapi.mappers;

import com.westminster.smartcampusapi.exceptions.RoomNotEmptyException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider; //tells Jersey to use this class to handle errors

//Part5.1 - Exception mapper for RoomNotEmptyException (409 Conflict)
@Provider 
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        return Response.status(Response.Status.CONFLICT) // 409
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\": \"" + exception.getMessage() + "\"}")
                .build();
    }
}