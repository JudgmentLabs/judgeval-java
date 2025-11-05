package com.judgmentlabs.judgeval.v1.evaluation;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

public final class EvaluationFactory {
    private final JudgmentSyncClient client;
    private final String             apiKey;
    private final String             organizationId;

    public EvaluationFactory(JudgmentSyncClient client, String apiKey, String organizationId) {
        this.client = client;
        this.apiKey = apiKey;
        this.organizationId = organizationId;
    }

    public Evaluation.Builder create() {
        return Evaluation.builder().client(client);
    }
}
