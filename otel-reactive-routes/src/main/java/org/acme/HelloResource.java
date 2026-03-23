package org.acme;
import io.quarkus.logging.Log;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class HelloResource {

    @RestClient
    CallHelloClient helloClient;

    @Route(path = "/hello", methods = Route.HttpMethod.GET)
    public String hello(RoutingExchange ex) {
        Log.info(ex.getHeader("traceparent"));
        return "bar";
    }

    @Route(path = "/callHello", methods = Route.HttpMethod.GET)
    public Uni<String> callHello() {
        return helloClient.callHello();
    }
}
