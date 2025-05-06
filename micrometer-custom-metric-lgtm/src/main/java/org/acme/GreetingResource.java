package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.inject.Inject;

@Path("/hello")
public class GreetingResource {

    @Inject
    MetricBean metricBean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        // increment twice
        metricBean.incrementVariableMagnitude();
        metricBean.incrementVariableMagnitude();

        System.out.println("request received");

        // decrement once
        metricBean.decrementVariableMagnitude();
        return "Hello from Quarkus REST";
    }
}
