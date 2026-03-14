package dev.matheuscruz.notification;

import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/notifications")
public class NotificationResource {

    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    public Response notification(SendEmailPayload sendEmailPayload) {

        Log.info(">>> Email sent to: " + sendEmailPayload.to());

        if (sendEmailPayload.accepted()) {
            Log.info(">>> Content: " + """
                    Congratulations, your proposal was accepted!
                    """);
        } else {
            Log.info(">>> Content: " + """
                    Hi, unfortunately your proposal was not accepted! Try it in another event.
                    """);
        }

        return Response.ok(Map.of("sent", true))
                .header("Content-Type", "application/json")
                .build();
    }
}
