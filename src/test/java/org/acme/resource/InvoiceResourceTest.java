package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Integration tests for {@link InvoiceResource}.
 * <p>
 * Uses Quarkus's {@code @QuarkusTest} to start the application in a test
 * container and sends real HTTP requests via REST Assured. Coverage includes:
 * <ul>
 *   <li>Successful calculation with same-currency line items</li>
 *   <li>Validation error responses (missing invoice, currency, date, lines)</li>
 *   <li>Server error for invalid/unrecognised currency codes</li>
 * </ul>
 * </p>
 */
@QuarkusTest
class InvoiceResourceTest {

    /**
     * Tests a successful request where all line items are in the same currency
     * as the invoice. Expects HTTP 200 and the correct sum.
     */
    @Test
    void testCalculateTotal_SameCurrency() {
        String requestBody = """
                {
                    "invoice": {
                        "currency": "USD",
                        "date": "2024-01-15",
                        "lines": [
                            {
                                "description": "Item 1",
                                "currency": "USD",
                                "amount": 100.00
                            },
                            {
                                "description": "Item 2",
                                "currency": "USD",
                                "amount": 250.50
                            }
                        ]
                    }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invoice/total")
                .then()
                .statusCode(200)
                .body("total", is(350.50F));
    }

    /**
     * Tests that a request with a missing (null) invoice field returns
     * HTTP 400 with an appropriate error message.
     */
    @Test
    void testCalculateTotal_MissingInvoice_Returns400() {
        String requestBody = """
                {
                    "currency": "USD",
                    "date": "2024-01-15",
                    "lines": []
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invoice/total")
                .then()
                .statusCode(400)
                .body("error", is("Invoice is required"));
    }

    /**
     * Tests that a request without a currency field returns HTTP 400.
     */
    @Test
    void testCalculateTotal_MissingCurrency_Returns400() {
        String requestBody = """
                {
                    "invoice": {
                        "date": "2024-01-15",
                        "lines": [
                            {
                                "description": "Item 1",
                                "currency": "USD",
                                "amount": 100.00
                            }
                        ]
                    }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invoice/total")
                .then()
                .statusCode(400)
                .body("error", is("Currency is required"));
    }

    /**
     * Tests that a request without a date field returns HTTP 400.
     */
    @Test
    void testCalculateTotal_MissingDate_Returns400() {
        String requestBody = """
                {
                    "invoice": {
                        "currency": "USD",
                        "lines": [
                            {
                                "description": "Item 1",
                                "currency": "USD",
                                "amount": 100.00
                            }
                        ]
                    }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invoice/total")
                .then()
                .statusCode(400)
                .body("error", is("Date is required"));
    }

    /**
     * Tests that a request with an empty lines array returns HTTP 400.
     */
    @Test
    void testCalculateTotal_MissingLines_Returns400() {
        String requestBody = """
                {
                    "invoice": {
                        "currency": "USD",
                        "date": "2024-01-15",
                        "lines": []
                    }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invoice/total")
                .then()
                .statusCode(400)
                .body("error", is("At least one line item is required"));
    }

    /**
     * Tests that an invalid/unrecognised currency code causes a server error
     * (HTTP 500) because the Frankfurter API will reject it.
     */
    @Test
    void testCalculateTotal_InvalidCurrency_Returns500() {
        String requestBody = """
                {
                    "invoice": {
                        "currency": "USD",
                        "date": "2024-01-15",
                        "lines": [
                            {
                                "description": "Item 1",
                                "currency": "XYZ",
                                "amount": 100.00
                            }
                        ]
                    }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invoice/total")
                .then()
                .statusCode(500)
                .body("error", containsString("Failed to calculate total"));
    }
}
