package org.acme.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.acme.model.FrankfurterResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "frankfurter-api")
public interface FrankfurterClient {

    @GET
    @Path("/{date}")
    FrankfurterResponse getRates(
            @PathParam("date") String date,
            @QueryParam("from") String from,
            @QueryParam("to") String to
    );
}
