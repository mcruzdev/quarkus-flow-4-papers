package dev.matheuscruz.c4p;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.flow.Flow;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.serverlessworkflow.api.types.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

import static io.serverlessworkflow.fluent.func.FuncWorkflowBuilder.workflow;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.function;

@ApplicationScoped
public class WaitReviewWorkflow extends Flow {

    private final ObjectMapper mapper;

    @ConfigProperty(name = "notification.service.url", defaultValue = "http://localhost:8082")
    private String notificationUrl;

    public WaitReviewWorkflow(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Workflow descriptor() {
        return workflow("waitReviewStatusForCommunication")
                .tasks(
                        function("updateProposalStatus", updateProposal(), ProposalReviewedEvent.class),
                        // TODO: It should be replaced by http().POST()...
                        function("requestNotification", message -> {
                            HttpRequest.Builder builder = HttpRequest.newBuilder();
                            try {
                                String body = mapper.writeValueAsString(message);
                                HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(body))
                                        .uri(URI.create(notificationUrl + "/api/notifications"))
                                        .header("Content-Type", "application/json")
                                        .header("Accept", "application/json")
                                        .build();
                                try (HttpClient client = HttpClient.newBuilder().build()) {
                                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                                    return response.body();
                                }
                            } catch (JsonProcessingException e) {
                                throw new UncheckedIOException("Error while serializing message", e);
                            } catch (IOException e) {
                                throw new UncheckedIOException("Error while sending request to notification service", e);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new RuntimeException("Error while sending request to notification service", e);
                            }
                        }, Message.class)
                )
                .build();
    }

    private static Function<ProposalReviewedEvent, Message> updateProposal() {
        return event -> {
            Proposal updatedProposal = QuarkusTransaction.requiringNew()
                    .call(() -> {
                        Proposal proposal = Proposal.findById(event.proposalId());
                        return proposal.accepted(event.accepted());
                    });

            return new Message(
                    updatedProposal.getSpeaker().getEmail(),
                    updatedProposal.getStatus() == ProposalStatus.ACCEPTED,
                    updatedProposal.getDescription());
        };
    }
}
