package com.westminster.smartcampusapi;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

// Part 1.1 -  Application Configuration 
// Used jakarta here istead of javax since it the newer version
@ApplicationPath("/api/v1") // Ensure this is exactly /api/v1
public class JakartaRestConfiguration extends ResourceConfig {
    public JakartaRestConfiguration() {
        // scan both packages
        // this will help Jersey to auto-registers all recourses and mappers
        packages("com.westminster.smartcampusapi.resources", 
                 "com.westminster.smartcampusapi.mappers");
    }
}