package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/hello")
public class GreetingResource {

    @Inject
    PercentilHistogram percentileHistogram;

    private static int callCount = 0;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
//        System.out.println("request received");
        double value = (callCount % 10) +1;
//        double value = 1;
        percentileHistogram.record(value);
        callCount++;
        return value + "";
    }
}
