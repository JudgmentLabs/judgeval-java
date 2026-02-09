package com.judgmentlabs.judgeval.evaluation;

import java.util.Objects;
import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

public final class Evaluation {
    private final JudgmentSyncClient client;
    private final Optional<String>   projectId;
    private final String             projectName;

    private Evaluation(Builder builder) {
        this.client = Objects.requireNonNull(builder.client, "client required");
        this.projectId = builder.projectId != null ? builder.projectId : Optional.empty();
        this.projectName = builder.projectName;
    }

    public Optional<String> getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public static Builder builder() {
        return new Builder();
    }

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

        public Evaluation build() {
            return new Evaluation(this);
        }
    }
}
