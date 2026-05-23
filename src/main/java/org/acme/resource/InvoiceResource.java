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

@Path("/invoice")
public class InvoiceResource {

    @Inject
    InvoiceService invoiceService;

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
