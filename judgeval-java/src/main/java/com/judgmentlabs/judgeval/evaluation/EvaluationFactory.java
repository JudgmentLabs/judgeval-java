package com.judgmentlabs.judgeval.evaluation;

import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

/**
 * Factory for creating evaluation builders.
 */
public final class EvaluationFactory {
    private final JudgmentSyncClient client;
    private final Optional<String>   projectId;
    private final String             projectName;

    public EvaluationFactory(JudgmentSyncClient client, Optional<String> projectId, String projectName) {
        this.client = client;
        this.projectId = projectId;
        this.projectName = projectName;
    }

    /**
     * Creates a new evaluation builder configured with this factory's client.
     *
     * @return a new evaluation builder
     */
    public Evaluation.Builder create() {
        return Evaluation.builder()
                .client(client)
                .projectId(projectId)
                .projectName(projectName);
    }
}
