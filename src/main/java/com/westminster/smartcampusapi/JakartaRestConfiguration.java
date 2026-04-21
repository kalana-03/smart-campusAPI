package com.westminster.smartcampusapi;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1") // Ensure this is exactly /api/v1
public class JakartaRestConfiguration extends ResourceConfig {
    public JakartaRestConfiguration() {
        // This tells Jersey where to find your Resources and Mappers
        packages("com.westminster.smartcampusapi.resources", 
                 "com.westminster.smartcampusapi.mappers");
    }
}