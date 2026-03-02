# Flow for Papers

_Call for papers application built with Quarkus Flow_

---

## Overview

Flow for Papers is a multi-service application that demonstrates event-driven workflows using Quarkus Flow, Apache Kafka, and AI-powered review. It simulates a conference Call for Papers (C4P) process where speakers submit proposals, an AI agent scores them, and speakers receive an acceptance or rejection notification by email.

### Services

| Service | Port | Description |
|---|---|---|
| c4p-service | 8080 | Accepts proposal submissions, manages proposal state, and sends notifications |
| review-service | 8081 | Listens for submitted proposals and scores them using an AI agent (Ollama) |
| notification-service | 8082 | Receives notification requests and logs email delivery |

### Architecture

```
Browser / curl
     |
     v
c4p-service (8080)
  - Receives proposal via POST /api/proposals
  - Runs SubmissionWorkflow: saves proposal to DB, emits CloudEvent to Kafka (flow-out)
  - Listens on proposal-reviewed topic for review results
  - Runs WaitReviewWorkflow: updates proposal status, calls notification-service

review-service (8081)
  - Listens on flow-out topic for submitted proposals
  - Runs ReviewProposalWorkflow: scores proposal via Ollama AI agent
  - Emits CloudEvent to Kafka (proposal-reviewed)

notification-service (8082)
  - Receives POST /api/notifications from c4p-service
  - Logs the acceptance or rejection email
```

---

## Prerequisites

Before running the application, make sure you have the following installed:

- Java 21 or later
- Apache Maven 3.9 or later
- Docker (required for Kafka dev services and the database)
- Ollama with a compatible model pulled locally (used by review-service for AI scoring)

### Pull an Ollama model

The review-service uses Ollama to score proposals. Pull a model before starting the services:

```shell
ollama pull llama3.2
```

Verify Ollama is running and the model is available:

```shell
ollama list
```

---

## Running the Application

Each service must be started in its own terminal. Quarkus Dev Services automatically provisions Kafka and the database via Docker, so no manual infrastructure setup is required.

### Step 1 - Start the notification-service

Open a terminal and run:

```shell
cd notification-service
./mvnw quarkus:dev
```

The service starts on port `8082`. Wait until you see the following line in the output:

```
Listening on: http://0.0.0.0:8082
```

### Step 2 - Start the review-service

Open a second terminal and run:

```shell
cd review-service
./mvnw quarkus:dev
```

The service starts on port `8081`. Because `quarkus.kafka.devservices.shared=true` is set in both services, the review-service will reuse the Kafka container started by the notification-service (or c4p-service if started first).

Wait until you see:

```
Listening on: http://0.0.0.0:8081
```

### Step 3 - Start the c4p-service

Open a third terminal and run:

```shell
cd c4p-service
./mvnw quarkus:dev
```

The service starts on port `8080` and also serves the Angular frontend via Quarkus Quinoa.

Wait until you see:

```
Listening on: http://0.0.0.0:8080
```

---

## Submitting a Proposal

Once all three services are running, you can submit a proposal using the web UI or via the command line.

### Option A - Web UI

Open your browser and navigate to:

```
http://localhost:8080
```

Use the proposal form to fill in the title, subject, description, and speaker details, then submit.

### Option B - Command line (good proposal)

The repository includes sample proposal payloads. To submit a proposal that is likely to be accepted (high-quality content):

```shell
curl -X POST http://localhost:8080/api/proposals \
  -H "Content-Type: application/json" \
  -d @c4p-service/src/main/resources/good-proposal.json
```

### Option C - Command line (bad proposal)

To submit a proposal that is likely to be rejected (low-quality content):

```shell
curl -X POST http://localhost:8080/api/proposals \
  -H "Content-Type: application/json" \
  -d @c4p-service/src/main/resources/bad-proposal.json
```

---

## Observing the Flow

After submitting a proposal, the following sequence of events occurs automatically:

1. **c4p-service** saves the proposal to the database and emits a `dev.matheuscruz.proposal.submitted` CloudEvent to the `flow-out` Kafka topic.
2. **review-service** consumes the event, invokes the Ollama AI agent to score the proposal (1-10), and emits a `dev.matheuscruz.proposal.reviewed` CloudEvent to the `proposal-reviewed` Kafka topic.
3. **c4p-service** consumes the review result, updates the proposal status to `ACCEPTED` or `REJECTED`, and calls `POST /api/notifications` on the notification-service.
4. **notification-service** logs the email that would be sent to the speaker.

You can follow the logs in each terminal to observe each step.

### Listing all proposals

```shell
curl http://localhost:8080/api/proposals
```

---

## Dev UI

Each service exposes the Quarkus Dev UI in development mode. It provides access to configuration, health checks, and extension-specific panels:

| Service | Dev UI URL |
|---|---|
| c4p-service | http://localhost:8080/q/dev/ |
| review-service | http://localhost:8081/q/dev/ |
| notification-service | http://localhost:8082/q/dev/ |

---

## Project Structure

```
quarkus-flow-4-papers/
  c4p-service/          # Main service: proposal submission, workflow orchestration, UI
  review-service/       # AI-powered proposal review service
  notification-service/ # Email notification service