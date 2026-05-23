package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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
}
