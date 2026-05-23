package org.acme.model;

import java.math.BigDecimal;

/**
 * Represents a single exchange rate response from the Frankfurter API v2
 * (<a href="https://frankfurter.dev">frankfurter.dev</a>).
 * <p>
 * The v2 endpoint returns a single rate pair. A typical response from
 * {@code GET /v2/rate/EUR/USD?date=2024-12-01} looks like:
 * <pre>{@code
 * {
 *   "date": "2024-12-01",
 *   "base": "EUR",
 *   "quote": "USD",
 *   "rate": 1.0556
 * }
 * }</pre>
 * </p>
 */
public class FrankfurterResponse {

    /** The date of the exchange rate in {@code YYYY-MM-DD} format. */
    private String date;

    /** The base (source) currency code. */
    private String base;

    /** The quote (target) currency code. */
    private String quote;

    /** The exchange rate value from {@link #base} to {@link #quote}. */
    private BigDecimal rate;

    /** Default constructor required for JSON deserialization. */
    public FrankfurterResponse() {
    }

    /**
     * Returns the date for which this exchange rate is valid.
     *
     * @return the date in {@code YYYY-MM-DD} format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date for the exchange rate.
     *
     * @param date the date in {@code YYYY-MM-DD} format
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the base (source) currency.
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
     * Returns the quote (target) currency.
     *
     * @return the ISO-4217 currency code
     */
    public String getQuote() {
        return quote;
    }

    /**
     * Sets the quote (target) currency.
     *
     * @param quote the ISO-4217 currency code
     */
    public void setQuote(String quote) {
        this.quote = quote;
    }

    /**
     * Returns the exchange rate value from {@link #base} to {@link #quote}.
     *
     * @return the exchange rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Sets the exchange rate value.
     *
     * @param rate the exchange rate
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
