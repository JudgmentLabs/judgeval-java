package com.judgmentlabs.judgeval.tracer;

import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

public final class TracerFactory {
    private final JudgmentSyncClient client;
    private final String             projectName;
    private final Optional<String>   projectId;

    public TracerFactory(JudgmentSyncClient client, String projectName, Optional<String> projectId) {
        this.client = client;
        this.projectName = projectName;
        this.projectId = projectId;
    }

    public Tracer.Builder create() {
        return Tracer.builder()
                .client(client)
                .projectName(projectName)
                .projectId(projectId);
    }
}
