package org.acme.model;

/**
 * Request wrapper for the invoice calculation endpoint.
 * <p>
 * This is the top-level JSON object received by {@code POST /invoice/total}.
 * It wraps the {@link Invoice} payload so the API contract is extensible
 * (e.g., future fields like metadata or user info can be added beside
 * the invoice without breaking the structure).
 * </p>
 */
public class InvoiceRequest {

    /** The invoice to calculate the total for. */
    private Invoice invoice;

    /** Default constructor required for JSON deserialization. */
    public InvoiceRequest() {
    }

    /**
     * Constructs a new request with the given invoice.
     *
     * @param invoice the invoice containing currency, date, and line items
     */
    public InvoiceRequest(Invoice invoice) {
        this.invoice = invoice;
    }

    /**
     * Returns the invoice payload.
     *
     * @return the invoice, or {@code null} if not set
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Sets the invoice payload.
     *
     * @param invoice the invoice containing currency, date, and line items
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
