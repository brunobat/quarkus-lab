package com.brunobat.ai.common;

import com.brunobat.ai.ChatService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BusinessResource {

    @Inject
    ChatService chat;
//    @Inject
//    QueueService queue;
//    @Inject
//    JobProcessor jobs;
//    @Inject
//    LongJobService longJobs;
//
//    private final Random rnd = new Random();

    @GET
    @Path("/chat")
    @Operation(
            summary = "Send a chat message after an async operation"
    )
    public Map<String, Object> chat(@QueryParam("q") String q) {
        if (q == null || q.isBlank()) {
            throw new BadRequestException("query param 'q' is required");
        }

//        jobs.processAsync(10 + rnd.nextInt(40));
        String answer = chat.answer(q);

        return Map.of("question", q, "answer", answer);
    }

//    @POST
//    @Path("/queue/push")
//    public Map<String, Object> push(String q) {
//        if (q == null || q.isBlank()) {
//            throw new BadRequestException("query param 'q' is required");
//        }
//
//        return Map.of("question", q, "enqueued?", queue.enqueue(q));
//    }
//
//    @POST
//    @Path("/queue/pop")
//    public Map<String, Object> pop() {
//        return queue.drainOne();
//    }
//
//    @POST
//    @Path("/long/start")
//    @Operation(
//            summary = "Will start the execution of an async task"
//    )
//    public Map<String, Object> startLong() {
//        long id = longJobs.startLongTask();
//        return Map.of("taskId", id, "status", "started");
//    }
}
