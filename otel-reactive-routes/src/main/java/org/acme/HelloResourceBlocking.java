package org.acme;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class HelloResourceBlocking {

    @RestClient
    CallHelloClientBlocking helloClient;

    @Route(path = "/helloBlocking", methods = Route.HttpMethod.GET)
    public String hello() {
        return "bar blocked";
    }

    @Route(path = "/callHelloBlocking", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    public String callHello() {
        return helloClient.callHello();
    }
}
