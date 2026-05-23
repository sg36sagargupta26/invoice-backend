package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class InvoiceResourceTest {

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
