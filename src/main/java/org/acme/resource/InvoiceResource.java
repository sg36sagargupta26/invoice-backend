package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Invoice;
import org.acme.model.InvoiceRequest;
import org.acme.service.InvoiceService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * REST resource for invoice operations.
 * <p>
 * Exposes endpoints under the {@code /invoice} base path for calculating
 * invoice totals with automatic currency conversion.
 * </p>
 */
@Path("/invoice")
public class InvoiceResource {

    /**
     * The service that handles invoice total calculation logic,
     * including currency conversion via the Frankfurter API.
     */
    @Inject
    InvoiceService invoiceService;

    /**
     * Calculates the total amount of an invoice in the specified base currency.
     * <p>
     * The request must contain a valid invoice with:
     * <ul>
     *   <li>A non-blank {@code currency} (ISO-4217 code)</li>
     *   <li>A non-blank {@code date} in {@code YYYY-MM-DD} format</li>
     *   <li>At least one line item, each with a description, currency, and positive amount</li>
     * </ul>
     * </p>
     *
     * <h4>HTTP Response Codes</h4>
     * <ul>
     *   <li><strong>200 OK</strong> — Total calculated successfully.
     *       Body: {@code { "total": 1234.56 }}</li>
     *   <li><strong>400 Bad Request</strong> — Missing or invalid fields
     *       (currency, date, lines). Body: {@code { "error": "..." }}</li>
     *   <li><strong>404 Not Found</strong> — Exchange rate not available
     *       for a given currency pair. Body: {@code { "error": "..." }}</li>
     *   <li><strong>500 Internal Server Error</strong> — Unexpected error
     *       during calculation. Body: {@code { "error": "..." }}</li>
     * </ul>
     *
     * @param request the request body containing the invoice payload
     * @return a {@link Response} with the total or an error message
     */
    @POST
    @Path("/total")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateTotal(InvoiceRequest request) {
        // Validate request
        if (request == null || request.getInvoice() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invoice is required"))
                    .build();
        }

        Invoice invoice = request.getInvoice();

        if (invoice.getCurrency() == null || invoice.getCurrency().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Currency is required"))
                    .build();
        }

        if (invoice.getDate() == null || invoice.getDate().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Date is required"))
                    .build();
        }

        if (invoice.getLines() == null || invoice.getLines().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "At least one line item is required"))
                    .build();
        }

        try {
            BigDecimal total = invoiceService.calculateTotal(request);
            return Response.ok(Map.of("total", total)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to calculate total: " + e.getMessage()))
                    .build();
        }
    }
}
