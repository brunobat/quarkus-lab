package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/helloBlocking")
@RegisterRestClient
public interface CallHelloClientBlocking {
    @GET
    String callHello();
}
