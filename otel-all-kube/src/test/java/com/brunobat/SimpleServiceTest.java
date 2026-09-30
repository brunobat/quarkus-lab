package com.brunobat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class SimpleServiceTest {

    @Test
    void testRootEndpoint() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }
}
