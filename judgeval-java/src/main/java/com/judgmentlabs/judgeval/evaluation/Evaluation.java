package com.judgmentlabs.judgeval.evaluation;

import java.util.Objects;
import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

/**
 * Evaluation configuration for running evaluations against traces and spans.
 */
public final class Evaluation {
    private final JudgmentSyncClient client;
    private final Optional<String>   projectId;
    private final String             projectName;

    private Evaluation(Builder builder) {
        this.client = Objects.requireNonNull(builder.client, "client required");
        this.projectId = Optional.ofNullable(builder.projectId).orElse(Optional.empty());
        this.projectName = builder.projectName;
    }

    public Optional<String> getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * Creates a new builder for configuring an Evaluation.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring and creating Evaluation instances.
     */
    public static final class Builder {
        private JudgmentSyncClient client;
        private Optional<String>   projectId;
        private String             projectName;

        Builder client(JudgmentSyncClient client) {
            this.client = client;
            return this;
        }

        Builder projectId(Optional<String> projectId) {
            this.projectId = projectId;
            return this;
        }

        Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        /**
         * Builds and returns a new Evaluation instance.
         *
         * @return the configured Evaluation
         */
        public Evaluation build() {
            return new Evaluation(this);
        }
    }
}
