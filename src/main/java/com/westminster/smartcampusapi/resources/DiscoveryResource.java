package com.westminster.smartcampusapi.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {
        Map<String, Object> response = new HashMap<>();
    
        // API Metadata
        response.put("name", "Smart Campus IoT System");
        response.put("version", "v1");
    
        // Discoverable Links
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors"); // build this later
        links.put("self", "/api/v1/");
    
        response.put("_links", links);

        return Response.ok(response).build();
    }
}