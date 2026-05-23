package org.acme.model;

import java.util.List;

/**
 * Represents an invoice with a target currency, an invoice date, and a list
 * of {@link LineItem line items}.
 * <p>
 * The {@code currency} field defines the <em>base currency</em> into which
 * all line-item amounts will be converted (if they are in a different currency).
 * The {@code date} field is used to look up historical exchange rates from the
 * Frankfurter API.
 * </p>
 */
public class Invoice {

    /** The base (target) currency for the invoice (e.g. {@code "USD"}). */
    private String currency;

    /** The invoice date in {@code YYYY-MM-DD} format, used for exchange rate lookup. */
    private String date;

    /** The line items on this invoice. */
    private List<LineItem> lines;

    /** Default constructor required for JSON deserialization. */
    public Invoice() {
    }

    /**
     * Constructs a new invoice with the given properties.
     *
     * @param currency the base currency (e.g. {@code "USD"})
     * @param date     the invoice date in {@code YYYY-MM-DD} format
     * @param lines    the line items on this invoice
     */
    public Invoice(String currency, String date, List<LineItem> lines) {
        this.currency = currency;
        this.date = date;
        this.lines = lines;
    }

    /**
     * Returns the base (target) currency for this invoice.
     *
     * @return the currency code, e.g. {@code "USD"}
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the base (target) currency for this invoice.
     *
     * @param currency the currency code, e.g. {@code "EUR"}
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Returns the invoice date.
     *
     * @return the date string in {@code YYYY-MM-DD} format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the invoice date.
     *
     * @param date the date string in {@code YYYY-MM-DD} format
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the list of line items on this invoice.
     *
     * @return the line items (may be {@code null} or empty)
     */
    public List<LineItem> getLines() {
        return lines;
    }

    /**
     * Sets the line items on this invoice.
     *
     * @param lines the line items to add
     */
    public void setLines(List<LineItem> lines) {
        this.lines = lines;
    }
}
