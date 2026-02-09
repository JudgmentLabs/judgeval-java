package com.judgmentlabs.judgeval.evaluation;

import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

public final class EvaluationFactory {
    private final JudgmentSyncClient client;
    private final Optional<String>   projectId;
    private final String             projectName;

    public EvaluationFactory(JudgmentSyncClient client, Optional<String> projectId, String projectName) {
        this.client = client;
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public Evaluation.Builder create() {
        return Evaluation.builder()
                .client(client)
                .projectId(projectId)
                .projectName(projectName);
    }
}
