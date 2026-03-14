package dev.matheuscruz.c4p.resource;

import dev.matheuscruz.c4p.application.data.ProposalSubmission;
import dev.matheuscruz.c4p.domain.Proposal;
import dev.matheuscruz.c4p.application.workflow.SubmissionWorkflow;
import io.quarkus.logging.Log;
import io.serverlessworkflow.impl.WorkflowModel;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/api/proposals")
@PermitAll
public class ProposalResource {

    final SubmissionWorkflow workflow;

    public ProposalResource(SubmissionWorkflow workflow) {
        this.workflow = workflow;
    }

    @POST
    @APIResponse(responseCode = "201", description = "Proposal submitted", headers = {
            @Header(name = "Location", description = "Location of the proposal created", schema = @Schema(type = SchemaType.STRING, examples = {
                    "/api/proposals/1" })) })
    public Response proposal(@Valid ProposalSubmission request) {

        WorkflowModel workflowModel = workflow.startInstance(request).await().indefinitely();

        ProposalSubmission submission = workflowModel.as(ProposalSubmission.class).orElseThrow();

        Log.info("Proposal persisted into the database: " + submission);

        return Response.status(201).entity(submission).header("Location", "/api/proposal/" + submission.id()).build();
    }

    @GET
    public Response all() {
        return Response.ok(Proposal.listAll()).build();
    }
}
