package com.westminster.smartcampusapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        response.put("admin_contact", "w2120137@westminster.ac.uk");
        response.put("developer", "Kalana Malhara");
        
        //primary resource collections (HATEOAS)
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        
        response.put("links", links);

        return Response.ok(response).build();
    }
}