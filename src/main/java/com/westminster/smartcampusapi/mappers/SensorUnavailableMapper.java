package com.westminster.smartcampusapi.mappers;

import com.westminster.smartcampusapi.exceptions.SensorUnavailableException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

//Part5.3 - Exception mapper for SensorUnavailableException(403 forbidden)
@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        return Response.status(Response.Status.FORBIDDEN) // 403
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\": \"" + exception.getMessage() + "\"}")
                .build();
    }
}