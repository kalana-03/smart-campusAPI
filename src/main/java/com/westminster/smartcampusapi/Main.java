package com.westminster.smartcampusapi;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    // The base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static HttpServer startServer() {
        // This tells Grizzly to look for your resources in the specified package
        final ResourceConfig rc = new ResourceConfig().packages("com.westminster.smartcampusapi.resources");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws Exception {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started at %sapi/v1\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}