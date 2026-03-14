package dev.matheuscruz.review;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import io.quarkus.logging.Log;
import io.serverlessworkflow.impl.events.EventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class ProposalReviewEventPublisher implements EventPublisher {

    private static final EventFormat FORMAT = EventFormatProvider.getInstance()
            .resolveFormat(JsonFormat.CONTENT_TYPE);

    @Inject
    @Channel("proposal.reviewed")
    Emitter<byte[]> eventEmitter;

    @Override
    public CompletableFuture<Void> publish(CloudEvent event) {
        try {
            if (event.getType().equals("dev.matheuscruz.proposal.reviewed")) {
                byte[] structured = FORMAT.serialize(event);
                Log.info("Sending event dev.matheuscruz.proposa.reviewed");
                return eventEmitter.send(structured).toCompletableFuture();
            }
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            final CompletableFuture<Void> cf = new CompletableFuture<>();
            cf.completeExceptionally(e);
            return cf;
        }
    }

    @Override
    public void close() {
    }
}
