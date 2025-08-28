package org.acme;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/howdy")
public class ManualSpanResource {

    @Inject
    Tracer tracer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        Span span = tracer.spanBuilder("mySpan").startSpan();

        try (Scope scope = span.makeCurrent()) {
            Log.info("hello-tracing");
            return "Hello from Quarkus REST";
        } catch (Throwable t) {
            span.recordException(t);
            return "failed";
        } finally {
            span.end();
        }
    }
}
