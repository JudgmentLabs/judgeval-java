package com.judgmentlabs.judgeval.tracer;

import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

/**
 * Factory for creating tracer builders.
 */
public final class TracerFactory {
    private final JudgmentSyncClient client;
    private final String             projectName;
    private final Optional<String>   projectId;

    public TracerFactory(JudgmentSyncClient client, String projectName, Optional<String> projectId) {
        this.client = client;
        this.projectName = projectName;
        this.projectId = projectId;
    }

    /**
     * Creates a new tracer builder configured with this factory's client.
     *
     * @return a new tracer builder
     */
    public Tracer.Builder create() {
        return Tracer.builder()
                .client(client)
                .projectName(projectName)
                .projectId(projectId);
    }
}
