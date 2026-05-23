package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.client.FrankfurterClient;
import org.acme.model.FrankfurterResponse;
import org.acme.model.Invoice;
import org.acme.model.InvoiceRequest;
import org.acme.model.LineItem;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service responsible for calculating invoice totals with automatic
 * currency conversion.
 * <p>
 * For line items in a currency different from the invoice's base currency,
 * the service fetches the exchange rate from the {@link FrankfurterClient}
 * (powered by the European Central Bank via the Frankfurter API) and
 * converts the amount before summing.
 * </p>
 */
@ApplicationScoped
public class InvoiceService {

    /**
     * The REST client for fetching exchange rates from the Frankfurter API.
     * Injected via MicroProfile REST Client with the {@code frankfurter-api} config key.
     */
    @Inject
    @RestClient
    FrankfurterClient frankfurterClient;

    /**
     * Calculates the total invoice amount by summing all line items after
     * converting them to the invoice's base currency.
     * <p>
     * Conversion logic:
     * <ul>
     *   <li>If a line item's currency matches the base currency, the amount
     *       is added directly.</li>
     *   <li>If the currencies differ, the service calls the Frankfurter API
     *       to get the exchange rate for the invoice date and multiplies
     *       the line-item amount by that rate.</li>
     * </ul>
     * Converted amounts are rounded to 4 decimal places during intermediate
     * steps, and the final total is rounded to 2 decimal places (standard
     * currency precision).
     * </p>
     *
     * @param request the request containing the invoice with currency, date,
     *                and line items
     * @return the total amount in the invoice's base currency, rounded to
     *         2 decimal places
     * @throws IllegalArgumentException if the exchange rate for any currency
     *                                  pair is not found in the API response
     */
    public BigDecimal calculateTotal(InvoiceRequest request) {
        Invoice invoice = request.getInvoice();
        String baseCurrency = invoice.getCurrency();
        String date = invoice.getDate();
        List<LineItem> lines = invoice.getLines();

        BigDecimal total = BigDecimal.ZERO;

        for (LineItem line : lines) {
            String lineCurrency = line.getCurrency();
            BigDecimal lineAmount = line.getAmount();

            if (lineCurrency.equalsIgnoreCase(baseCurrency)) {
                // Same currency, no conversion needed
                total = total.add(lineAmount);
            } else {
                // Fetch exchange rate from Frankfurter API v2
                FrankfurterResponse response = frankfurterClient.getRate(lineCurrency, baseCurrency, date);
                BigDecimal rate = response.getRate();

                if (rate == null) {
                    throw new IllegalArgumentException("Exchange rate not found for " + lineCurrency + " -> " + baseCurrency);
                }

                // Convert line amount to base currency: amount * rate, rounded to 4 decimal places
                BigDecimal convertedAmount = lineAmount.multiply(rate).setScale(4, RoundingMode.HALF_UP);
                total = total.add(convertedAmount);
            }
        }

        // Final total rounded to 2 decimal places
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
