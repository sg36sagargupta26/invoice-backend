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

@ApplicationScoped
public class InvoiceService {

    @Inject
    @RestClient
    FrankfurterClient frankfurterClient;

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
                // Fetch exchange rate from Frankfurter API
                FrankfurterResponse response = frankfurterClient.getRates(date, lineCurrency, baseCurrency);
                BigDecimal rate = response.getRates().get(baseCurrency);

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
