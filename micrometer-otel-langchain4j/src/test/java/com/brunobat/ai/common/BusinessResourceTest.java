package com.brunobat.ai.common;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class BusinessResourceTest {

    // Exercises the /long/start endpoint which submits an async job and does
    // not call the LLM, so it runs without a live OpenAI backend.
    @Test
    void testStartLongEndpoint() {
        given()
          .when().post("/long/start")
          .then()
             .statusCode(200)
             .body("status", is("started"));
    }
}
