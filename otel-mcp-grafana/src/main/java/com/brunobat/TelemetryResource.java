package com.brunobat;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/telemetry")
public class TelemetryResource {

    @Inject
    TempoMcpClient tempoMcp;

    /** Search for this application's traces via Tempo MCP. */
    @GET
    @Path("/traces")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode myTraces() {
        return tempoMcp.searchTraces("{resource.service.name = \"quarkus-mcp-telemetry\"}");
    }

    /** Run an arbitrary TraceQL query via Tempo MCP. */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode search(@QueryParam("q") String query) {
        if (query == null || query.isBlank()) {
            query = "{}";
        }
        return tempoMcp.searchTraces(query);
    }

    /** Get a specific trace by ID. */
    @GET
    @Path("/trace")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode trace(@QueryParam("id") String traceId) {
        return tempoMcp.getTrace(traceId);
    }

    /** List available attribute names from Tempo. */
    @GET
    @Path("/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode attributes(@QueryParam("scope") String scope) {
        return tempoMcp.getAttributeNames(scope);
    }

    /** List services that have sent traces. */
    @GET
    @Path("/services")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode services() {
        return tempoMcp.getAttributeValues("resource.service.name");
    }
}
