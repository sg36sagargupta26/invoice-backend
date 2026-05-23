package org.acme.model;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents the JSON response from the Frankfurter API
 * (<a href="https://www.frankfurter.app">frankfurter.app</a>).
 * <p>
 * The Frankfurter API publishes daily exchange rates published by
 * the European Central Bank. A typical response looks like:
 * <pre>{@code
 * {
 *   "amount": 1.0,
 *   "base": "EUR",
 *   "date": "2024-01-15",
 *   "rates": { "USD": 1.10, "GBP": 0.86, ... }
 * }
 * }</pre>
 * </p>
 */
public class FrankfurterResponse {

    /** The amount that was converted (usually {@code 1.0}). */
    private BigDecimal amount;

    /** The base currency of the response (the {@code from} parameter). */
    private String base;

    /** The date of the exchange rates in {@code YYYY-MM-DD} format. */
    private String date;

    /** A map of target currency codes to their exchange rates against {@link #base}. */
    private Map<String, BigDecimal> rates;

    /** Default constructor required for JSON deserialization. */
    public FrankfurterResponse() {
    }

    /**
     * Returns the source amount used by the API.
     *
     * @return the amount (typically {@code 1.0})
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the source amount.
     *
     * @param amount the amount used by the API
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Returns the base (source) currency of the exchange rates.
     *
     * @return the ISO-4217 currency code
     */
    public String getBase() {
        return base;
    }

    /**
     * Sets the base (source) currency.
     *
     * @param base the ISO-4217 currency code
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * Returns the date for which these exchange rates are valid.
     *
     * @return the date in {@code YYYY-MM-DD} format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date for the exchange rates.
     *
     * @param date the date in {@code YYYY-MM-DD} format
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the exchange rates.
     * <p>
     * The map keys are ISO-4217 currency codes, and the values are the
     * exchange rate from {@link #base} to each target currency.
     * </p>
     *
     * @return a map of currency → rate, or {@code null} if not set
     */
    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    /**
     * Sets the exchange rates.
     *
     * @param rates a map of target currency codes to exchange rates
     */
    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
