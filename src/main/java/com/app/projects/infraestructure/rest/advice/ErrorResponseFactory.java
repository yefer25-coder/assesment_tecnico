package com.app.projects.infraestructure.rest.advice;

import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;

public class ErrorResponseFactory {

    public static ProblemDetail createProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now().toString());
        problem.setProperty("traceId", UUID.randomUUID().toString());
        problem.setProperty("instance", request.getDescription(false).replace("uri=", ""));

        return problem;
    }
}
