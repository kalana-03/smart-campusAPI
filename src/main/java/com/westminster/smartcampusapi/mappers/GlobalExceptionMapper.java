package com.westminster.smartcampusapi.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

//Part 5.4 - Global Safety Net(500 Internal Server Error)
//catch all exceptionmapper that intercepts any unexpected runtime error
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {

// for catching error
                // Create a structured error response
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "An unexpected server error occurred.");
        errorDetails.put("message", "Please contact the system administrator.");
        errorDetails.put("status", "500");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorDetails) //converts this to clean JSON
                .build();
    }
}