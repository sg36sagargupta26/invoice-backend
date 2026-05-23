package org.acme.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.acme.model.FrankfurterResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client for the <a href="https://frankfurter.dev">Frankfurter API</a>.
 * <p>
 * Frankfurter provides daily exchange rates blended from multiple central banks.
 * This client targets the v2 API and is registered under the {@code frankfurter-api}
 * configuration key, with the base URL configured in {@code application.properties}:
 * <pre>{@code
 * frankfurter-api/mp-rest/url=https://api.frankfurter.dev/v2
 * }</pre>
 * <p>
 * The v2 endpoint {@code GET /rate/{base}/{quote}?date=...} returns a single
 * exchange rate pair.
 * </p>
 *
 * @see FrankfurterResponse
 */
@Path("/")
@RegisterRestClient(configKey = "frankfurter-api")
public interface FrankfurterClient {

    /**
     * Fetches a single exchange rate from the Frankfurter API v2.
     * <p>
     * Example request: {@code GET https://api.frankfurter.dev/v2/rate/EUR/USD?date=2024-01-15}
     * </p>
     *
     * @param base  the source (base) currency code, e.g. {@code "EUR"}
     * @param quote the target (quote) currency code, e.g. {@code "USD"}
     * @param date  the date in {@code YYYY-MM-DD} format for which to fetch the rate
     * @return a {@link FrankfurterResponse} containing the exchange rate pair
     */
    @GET
    @Path("/rate/{base}/{quote}")
    FrankfurterResponse getRate(
            @PathParam("base") String base,
            @PathParam("quote") String quote,
            @QueryParam("date") String date
    );
}
