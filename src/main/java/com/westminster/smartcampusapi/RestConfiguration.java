package com.westminster.smartcampusapi;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/api/v1")
public class RestConfiguration extends ResourceConfig {
    public RestConfiguration() {
        packages("com.westminster.smartcampusapi.resources",
                 "com.westminster.smartcampusapi.mappers");
    }
}