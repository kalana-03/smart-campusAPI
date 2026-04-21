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
    public Response getDiscoveryInfo() {
        Map<String, Object> response = new HashMap<>();
        
        // 1. Versioning & Meta
        response.put("api_version", "1.0.0");
        
        // 2. REQUIRED: Administrative Contact
        response.put("admin_contact", "your_id@westminster.ac.uk");
        response.put("developer", "Your Name");
        
        // 3. Primary Resource Collections (HATEOAS)
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        
        response.put("links", links);

        return Response.ok(response).build();
    }
}