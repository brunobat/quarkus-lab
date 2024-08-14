package org.acme;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.Arrays;

@Path("/roll-dice")
public class HistogramResource {

    private static final Logger LOG = Logger.getLogger(HistogramResource.class);

    private final LongHistogram rolls;

    public HistogramResource(Meter meter) {
        rolls = meter.histogramBuilder("hello.roll.dice")
                .ofLongs() // Required to get a LongHistogram, default is DoubleHistogram
                .setDescription("A distribution of the value of the rolls.")
                .setExplicitBucketBoundariesAdvice(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L))
                .setUnit("points")
                .build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloGauge() {
        var roll = roll();
        rolls.record(roll,
                Attributes.of(AttributeKey.stringKey("attribute.name"), "value"));
        LOG.info("roll-dice: " + roll);
        return "" + roll;
    }

    public long roll() {
        return (long) (Math.random() * 6) + 1;
    }
}