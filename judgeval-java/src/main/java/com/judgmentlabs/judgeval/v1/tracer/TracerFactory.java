package com.judgmentlabs.judgeval.v1.tracer;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

public final class TracerFactory {
    private final JudgmentSyncClient client;
    private final String             apiKey;
    private final String             organizationId;
    private final String             apiUrl;

    public TracerFactory(JudgmentSyncClient client, String apiKey, String organizationId, String apiUrl) {
        this.client = client;
        this.apiKey = apiKey;
        this.organizationId = organizationId;
        this.apiUrl = apiUrl;
    }

    public Tracer.Builder create() {
        return Tracer.builder()
                .apiKey(apiKey)
                .organizationId(organizationId)
                .apiUrl(apiUrl);
    }
}
