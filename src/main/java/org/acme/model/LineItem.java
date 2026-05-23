package org.acme.model;

import java.math.BigDecimal;

/**
 * A single line item on an invoice.
 * <p>
 * Each line item has a {@link #description}, a {@link #currency} (which may
 * differ from the invoice's base currency), and an {@link #amount}.
 * When the line-item currency differs from the invoice currency, the service
 * will convert the amount using the exchange rate for the invoice date.
 * </p>
 */
public class LineItem {

    /** A short description of the line item (e.g. {@code "Consulting Services"}). */
    private String description;

    /** The ISO-4217 currency code for this line item (e.g. {@code "EUR"}). */
    private String currency;

    /** The monetary amount for this line item. */
    private BigDecimal amount;

    /** Default constructor required for JSON deserialization. */
    public LineItem() {
    }

    /**
     * Constructs a new line item with the given properties.
     *
     * @param description a short description of the item
     * @param currency    the ISO-4217 currency code
     * @param amount      the monetary amount
     */
    public LineItem(String description, String currency, BigDecimal amount) {
        this.description = description;
        this.currency = currency;
        this.amount = amount;
    }

    /**
     * Returns the line-item description.
     *
     * @return the description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the line-item description.
     *
     * @param description a short description of the item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the ISO-4217 currency code for this line item.
     *
     * @return the currency code, e.g. {@code "USD"}
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the ISO-4217 currency code for this line item.
     *
     * @param currency the currency code, e.g. {@code "GBP"}
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Returns the line-item amount.
     *
     * @return the monetary amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the line-item amount.
     *
     * @param amount the monetary amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
