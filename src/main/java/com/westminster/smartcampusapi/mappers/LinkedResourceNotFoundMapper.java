package com.westminster.smartcampusapi.mappers;

import com.westminster.smartcampusapi.exceptions.LinkedResourceNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        Map<String, String> errorData = new HashMap<>();
        errorData.put("error", exception.getMessage());
        errorData.put("code", "RESOURCE_UNLINKED");

        return Response.status(422) // Unprocessable Entity
                .type(MediaType.APPLICATION_JSON)
                .entity(errorData) // Jersey handles the JSON conversion
                .build();
}
}