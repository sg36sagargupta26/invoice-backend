package org.acme.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Custom CORS filter for both preflight (OPTIONS) and actual requests.
 * <p>
 * The {@code @PreMatching} annotation ensures that OPTIONS preflight requests
 * are intercepted <em>before</em> JAX-RS method matching occurs. Without it,
 * RESTEasy Reactive may route the OPTIONS to its built-in handler, which
 * does not add CORS headers.
 * </p>
 *
 * <h4>Why this is needed</h4>
 * Quarkus's built-in {@code quarkus.http.cors} configuration (set in
 * {@code application.properties}) sometimes fails to intercept preflight
 * requests when RESTEasy Reactive handles them first. This filter provides
 * a reliable fallback that guarantees CORS headers on all responses.
 */
@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    /** The only origin allowed to access this API. */
    private static final String ALLOWED_ORIGIN = "http://localhost:3000";

    // ================================================================
    // Preflight (OPTIONS) handler
    // ================================================================

    /**
     * Intercepts preflight {@code OPTIONS} requests and returns a
     * {@code 200 OK} with CORS headers immediately, without passing
     * through to the REST resource.
     *
     * @param requestContext the incoming request context
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Only intercept OPTIONS (preflight) requests
        if (!"OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        String origin = requestContext.getHeaderString("Origin");
        if (origin == null || origin.isBlank()) {
            return;
        }

        // Build a preflight response with standard CORS headers
        Response.ResponseBuilder builder = Response.ok()
                .header("Access-Control-Allow-Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .header("Access-Control-Max-Age", "86400") // 24 hours
                .header("Access-Control-Allow-Credentials", "true");

        requestContext.abortWith(builder.build());
    }

    // ================================================================
    // Actual-response CORS header appender
    // ================================================================

    /**
     * Appends CORS headers to every outgoing response so that the browser
     * allows cross-origin reads. This handles all non-preflight requests
     * (POST, GET, etc.).
     *
     * @param requestContext  the incoming request context
     * @param responseContext the outgoing response context
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");
        if (origin == null || origin.isBlank()) {
            return;
        }

        responseContext.getHeaders().add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Expose-Headers", "Location, Content-Type");
    }
}
