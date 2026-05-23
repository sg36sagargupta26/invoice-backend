package org.acme.model;

public class InvoiceRequest {

    private Invoice invoice;

    public InvoiceRequest() {
    }

    public InvoiceRequest(Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
