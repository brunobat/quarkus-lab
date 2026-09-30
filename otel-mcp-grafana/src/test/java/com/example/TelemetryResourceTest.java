package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static io.restassured.RestAssured.given;

// Disabled: every test here calls /telemetry/* endpoints that proxy to a live Grafana
// Tempo MCP server (quarkus.langchain4j.mcp.tempo.url=http://localhost:3200/api/mcp).
// They assume the Grafana LGTM stack is already running; without it the MCP client fails
// with "Connection refused: localhost:3200". Bring the stack up and remove @Disabled to run them.
@Disabled("Requires a running Grafana LGTM/Tempo stack with an MCP server on localhost:3200")
@QuarkusTest
class TelemetryResourceTest {

    AtomicBoolean started = new AtomicBoolean(false);

    @BeforeEach
    void init() {
        // make sure there will be data in Grafana
        if (!started.compareAndSet(false, true)) {
            given()
                    .when().get("/hello")
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    void testMyTracesReturnsJson() {
        given()
                .when().get("/telemetry/traces")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void testSearchWithQueryReturnsJson() {
        given()
                .queryParam("q", "{}")
                .when().get("/telemetry/search")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void testSearchWithBlankQueryDefaultsToAll() {
        given()
                .queryParam("q", "  ")
                .when().get("/telemetry/search")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void testSearchWithNoQueryDefaultsToAll() {
        given()
                .when().get("/telemetry/search")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void testGetTraceById() {
        // First search for traces to get a real trace ID
        String traceId = given()
                .when().get("/telemetry/traces")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("traces[0].traceID");

        if (traceId != null) {
            given()
                    .queryParam("id", traceId)
                    .when().get("/telemetry/trace")
                    .then()
                    .statusCode(200)
                    .contentType("application/json");
        }
    }

    @Test
    void testAttributesWithScope() {
        given()
                .queryParam("scope", "span")
                .when().get("/telemetry/attributes")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void testAttributesWithoutScope() {
        given()
                .when().get("/telemetry/attributes")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void testServicesReturnsJson() {
        given()
                .when().get("/telemetry/services")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }
}
