package com.westminster.smartcampusapi.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

//Part 1.2 - The ”Discovery” endpoint
//root resource that provide API metadata, version info and admin contact
@Path("/") 
public class DiscoveryResource {
    
    //Part 1.2 - GET /api/v1/
    // Returns API metadata   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo() {
        Map<String, Object> response = new HashMap<>();
        
        //version and meta
        response.put("api_version", "1.0.0");
        
        //administrative contact
        response.put("admin_contact", "your_id@westminster.ac.uk");
        response.put("developer", "Your Name");
        
        //primary resource collections (HATEOAS)
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        
        response.put("links", links);

        return Response.ok(response).build();
    }
}