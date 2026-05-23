package org.acme.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.acme.model.FrankfurterResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client for the <a href="https://www.frankfurter.app">Frankfurter API</a>.
 * <p>
 * Frankfurter provides daily exchange rates published by the European Central Bank.
 * This client is registered under the {@code frankfurter-api} configuration key,
 * with the base URL configured in {@code application.properties}:
 * <pre>{@code
 * frankfurter-api/mp-rest/url=https://api.frankfurter.app
 * }</pre>
 * </p>
 *
 * @see FrankfurterResponse
 */
@Path("/")
@RegisterRestClient(configKey = "frankfurter-api")
public interface FrankfurterClient {

    /**
     * Fetches exchange rates for a specific date from the Frankfurter API.
     * <p>
     * Example request: {@code GET https://api.frankfurter.app/2024-01-15?from=EUR&to=USD}
     * </p>
     *
     * @param date the date in {@code YYYY-MM-DD} format for which to fetch rates
     * @param from the source (base) currency code, e.g. {@code "EUR"}
     * @param to   the target currency code, e.g. {@code "USD"}
     * @return a {@link FrankfurterResponse} containing the exchange rate for the target currency
     */
    @GET
    @Path("/{date}")
    FrankfurterResponse getRates(
            @PathParam("date") String date,
            @QueryParam("from") String from,
            @QueryParam("to") String to
    );
}
