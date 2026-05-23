package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A simple greeting resource provided by the Quarkus starter template.
 * <p>
 * This class can be removed once the invoice-related endpoints are the
 * primary focus of the application.
 * </p>
 */
@Path("/hello")
public class GreetingResource {

    /**
     * Returns a simple greeting message.
     *
     * @return the string {@code "Hello from Quarkus REST"}
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }
}
