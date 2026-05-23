package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    public Map<String, BigDecimal> calculateTotal(InvoiceRequest request) {
        BigDecimal total = invoiceService.calculateTotal(request);
        return Map.of("total", total);
    }
}
