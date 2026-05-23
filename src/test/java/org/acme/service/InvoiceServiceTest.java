package org.acme.service;

import org.acme.client.FrankfurterClient;
import org.acme.model.FrankfurterResponse;
import org.acme.model.Invoice;
import org.acme.model.InvoiceRequest;
import org.acme.model.LineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link InvoiceService}.
 * <p>
 * Uses Mockito to mock the {@link FrankfurterClient} so that tests do not
 * make real HTTP calls to the Frankfurter API. Coverage includes:
 * <ul>
 *   <li>Same-currency line items (no conversion needed)</li>
 *   <li>Single-currency conversion</li>
 *   <li>Mixed currencies (some same, some converted)</li>
 *   <li>Missing exchange rate (should throw {@link IllegalArgumentException})</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    /** Mock of the Frankfurter REST client, injected into the service. */
    @Mock
    private FrankfurterClient frankfurterClient;

    /** The service under test. */
    private InvoiceService invoiceService;

    /**
     * Sets up the service before each test.
     * Creates a new {@link InvoiceService} instance and injects the mock client
     * via direct field assignment (the service is not using CDI in this test).
     */
    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService();
        invoiceService.frankfurterClient = frankfurterClient;
    }

    /**
     * Tests that when all line items use the same currency as the invoice,
     * the amounts are summed directly without any API call.
     */
    @Test
    void testCalculateTotal_SameCurrency() {
        // Given: all line items are in the same currency as the invoice
        Invoice invoice = new Invoice();
        invoice.setCurrency("USD");
        invoice.setDate("2024-01-15");

        LineItem item1 = new LineItem();
        item1.setDescription("Item 1");
        item1.setCurrency("USD");
        item1.setAmount(new BigDecimal("100.00"));

        LineItem item2 = new LineItem();
        item2.setDescription("Item 2");
        item2.setCurrency("USD");
        item2.setAmount(new BigDecimal("250.50"));

        invoice.setLines(List.of(item1, item2));

        InvoiceRequest request = new InvoiceRequest();
        request.setInvoice(invoice);

        // When
        BigDecimal total = invoiceService.calculateTotal(request);

        // Then
        assertEquals(new BigDecimal("350.50"), total);
    }

    /**
     * Tests that a line item in a different currency is correctly converted
     * to the invoice's base currency using the mocked exchange rate.
     */
    @Test
    void testCalculateTotal_WithCurrencyConversion() {
        // Given: line items in different currencies
        Invoice invoice = new Invoice();
        invoice.setCurrency("USD");
        invoice.setDate("2024-01-15");

        LineItem item1 = new LineItem();
        item1.setDescription("Item in EUR");
        item1.setCurrency("EUR");
        item1.setAmount(new BigDecimal("100.00"));

        invoice.setLines(List.of(item1));

        InvoiceRequest request = new InvoiceRequest();
        request.setInvoice(invoice);

        // Mock Frankfurter API response: 1 EUR = 1.10 USD
        FrankfurterResponse mockResponse = new FrankfurterResponse();
        mockResponse.setAmount(new BigDecimal("100.00"));
        mockResponse.setBase("EUR");
        mockResponse.setDate("2024-01-15");
        mockResponse.setRates(Map.of("USD", new BigDecimal("1.10")));

        when(frankfurterClient.getRates(eq("2024-01-15"), eq("EUR"), eq("USD"))).thenReturn(mockResponse);

        // When
        BigDecimal total = invoiceService.calculateTotal(request);

        // Then: 100 EUR * 1.10 = 110.00 USD
        assertEquals(new BigDecimal("110.00"), total);
    }

    /**
     * Tests a mix of same-currency and cross-currency line items.
     * Same-currency items are added directly; cross-currency items are
     * converted using the mocked exchange rate.
     */
    @Test
    void testCalculateTotal_MixedCurrencies() {
        // Given: mix of same and different currencies
        Invoice invoice = new Invoice();
        invoice.setCurrency("USD");
        invoice.setDate("2024-01-15");

        LineItem item1 = new LineItem();
        item1.setDescription("Local item");
        item1.setCurrency("USD");
        item1.setAmount(new BigDecimal("50.00"));

        LineItem item2 = new LineItem();
        item2.setDescription("EUR item");
        item2.setCurrency("EUR");
        item2.setAmount(new BigDecimal("200.00"));

        invoice.setLines(List.of(item1, item2));

        InvoiceRequest request = new InvoiceRequest();
        request.setInvoice(invoice);

        // Mock Frankfurter API response: 1 EUR = 1.10 USD
        FrankfurterResponse mockResponse = new FrankfurterResponse();
        mockResponse.setAmount(new BigDecimal("200.00"));
        mockResponse.setBase("EUR");
        mockResponse.setDate("2024-01-15");
        mockResponse.setRates(Map.of("USD", new BigDecimal("1.10")));

        when(frankfurterClient.getRates(eq("2024-01-15"), eq("EUR"), eq("USD"))).thenReturn(mockResponse);

        // When
        BigDecimal total = invoiceService.calculateTotal(request);

        // Then: 50.00 + (200 * 1.10 = 220.00) = 270.00 USD
        assertEquals(new BigDecimal("270.00"), total);
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the
     * Frankfurter API response does not contain a rate for the requested
     * currency pair (e.g., empty rates map).
     */
    @Test
    void testCalculateTotal_RateNotFound() {
        // Given: a line item with a currency that has no exchange rate
        Invoice invoice = new Invoice();
        invoice.setCurrency("GBP");
        invoice.setDate("2024-01-15");

        LineItem item1 = new LineItem();
        item1.setDescription("Item in EUR");
        item1.setCurrency("EUR");
        item1.setAmount(new BigDecimal("100.00"));

        invoice.setLines(List.of(item1));

        InvoiceRequest request = new InvoiceRequest();
        request.setInvoice(invoice);

        // Mock Frankfurter API response with empty rates
        FrankfurterResponse mockResponse = new FrankfurterResponse();
        mockResponse.setAmount(new BigDecimal("100.00"));
        mockResponse.setBase("EUR");
        mockResponse.setDate("2024-01-15");
        mockResponse.setRates(Map.of());

        when(frankfurterClient.getRates(eq("2024-01-15"), eq("EUR"), eq("GBP"))).thenReturn(mockResponse);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> invoiceService.calculateTotal(request));
    }
}
