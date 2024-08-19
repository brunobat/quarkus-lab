package org.acme;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.Arrays;

@Path("/hello-metrics")
public class MetricResource {

    private static final Logger LOG = Logger.getLogger(MetricResource.class);

    private final Meter meter;
    private final LongCounter counter;
    private final LongHistogram rolls;

    public MetricResource(Meter meter) {
        this.meter = meter;
        counter = meter.counterBuilder("hello-metrics")
                .setDescription("hello-metrics")
                .setUnit("invocations")
                .build();
        meter.gaugeBuilder("jvm.memory.total")
                .setDescription("Reports JVM memory usage.")
                .setUnit("byte")
                .ofLongs()
                .buildWithCallback(
                        result -> result.record(
                                Runtime.getRuntime().totalMemory(),
                                Attributes.empty()));
        rolls = meter.histogramBuilder("hello.roll.dice")
                .ofLongs() // Required to get a LongHistogram, default is DoubleHistogram
                .setDescription("A distribution of the value of the rolls.")
                .setExplicitBucketBoundariesAdvice(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L))
                .setUnit("points")
                .build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        counter.add(1,
                Attributes.of(AttributeKey.stringKey("attribute.name"), "value"));
        LOG.info("hello-metrics");
        return "hello-metrics";
    }

    @GET
    @Path("/roll-dice")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloGauge() {
        var roll = roll();
        rolls.record(roll, Attributes.of(AttributeKey.stringKey("attribute.name"), "value"));
        LOG.info("roll-dice: " + roll);
        return "" + roll;
    }

    public long roll() {
        return (long) (Math.random() * 6) + 1;
    }
}