package dev.matheuscruz.c4p;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class SubmissionReviewedListener {


    @Incoming("submission-reviews")
    public void handleSubmissionReviewed(Object event) {
        Log.info("Submission reviewed event: " + event);
    }
}
