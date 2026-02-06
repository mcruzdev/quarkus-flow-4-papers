package dev.matheuscruz.api;

import dev.matheuscruz.api.data.ProposalDTO;
import dev.matheuscruz.api.model.Proposal;
import dev.matheuscruz.api.workflow.SubmissionWorkflow;
import io.serverlessworkflow.impl.WorkflowApplication;
import io.serverlessworkflow.impl.WorkflowModel;
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
public class ProposalResource {

    final WorkflowApplication app;
    final SubmissionWorkflow workflow;

    public ProposalResource(WorkflowApplication app, SubmissionWorkflow workflow) {
        this.app = app;
        this.workflow = workflow;
    }

    @POST
    @APIResponse(responseCode = "201", description = "Proposal submitted", headers = {
            @Header(name = "Location", description = "Location of the proposal created", schema = @Schema(type = SchemaType.STRING, examples = {"/api/proposals/1"}))})
    public Response proposal(@Valid ProposalDTO request) {

        // ignore the output (WorkflowModel) for now
        WorkflowModel workflowModel = workflow.instance(request).start().join();

        ProposalDTO proposal = workflowModel.as(ProposalDTO.class).orElseThrow();

        return Response.status(201).entity(proposal).header("Location", "/api/proposal/" + proposal.id()).build();
    }

    @GET
    public Response all() {
        return Response.ok(Proposal.listAll()).build();
    }
}
