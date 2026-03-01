package dev.matheuscruz.c4p;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.flow.Flow;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.serverlessworkflow.api.types.Workflow;
import jakarta.enterprise.context.Dependent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

import static io.serverlessworkflow.fluent.func.FuncWorkflowBuilder.workflow;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.function;

@Dependent
public class WaitReviewWorkflow extends Flow {

    private final ObjectMapper mapper;

    public WaitReviewWorkflow(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Workflow descriptor() {
        return workflow("waitReviewStatusForCommunication")
                .tasks(
                        function("updateProposalStatus", updateProposal(), ProposalReviewedEvent.class),
                        function("requestNotification", o -> {
                            HttpRequest.Builder builder = HttpRequest.newBuilder();
                            try {
                                String body = mapper.writeValueAsString(o);
                                HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(body))
                                        .uri(URI.create("http://localhost:8082/api/notifications"))
                                        .build();
                                try (HttpClient client = HttpClient.newBuilder().build()) {
                                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                                    return response.body();
                                }
                            } catch (IOException e) {
                                throw new UncheckedIOException("Error while sending request to notification service", e);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new RuntimeException("Error while sending request to notification service", e);
                            }
                        }, Message.class)
//                        http()
//                                .POST()
//                                .uri(URI.create("http://localhost:8082/api/notifications"))
//                                .header("Content-Type", "application/json")
//                                .body("{ to: .message.to }").inputFrom((object, workflowContext) -> {
//                                    Log.info("Message: " + object);
//                                    return object;
//                                }, Message.class)
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
