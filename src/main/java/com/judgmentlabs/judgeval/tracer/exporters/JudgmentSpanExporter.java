package com.judgmentlabs.judgeval.tracer.exporters;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.judgmentlabs.judgeval.tracer.OpenTelemetryKeys;
import com.judgmentlabs.judgeval.utils.Logger;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.data.DelegatingSpanData;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public class JudgmentSpanExporter implements SpanExporter {
    private final SpanExporter delegate;
    private final String projectId;

    public JudgmentSpanExporter(String endpoint, String apiKey, String organizationId, String projectId) {
        if (projectId == null || projectId.isEmpty()) {
            throw new IllegalArgumentException("projectId is required for JudgmentSpanExporter");
        }
        this.delegate = OtlpHttpSpanExporter.builder()
                .setEndpoint(endpoint)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("X-Organization-Id", organizationId)
                .build();
        this.projectId = projectId;
    }

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        Logger.info("JudgmentSpanExporter exporting spans: count=" + (spans != null ? spans.size() : 0));
        AttributeKey<String> key = AttributeKey.stringKey(OpenTelemetryKeys.ResourceKeys.JUDGMENT_PROJECT_ID);
        Attributes extra = Attributes.of(key, projectId);
        Resource added = Resource.create(extra);
        List<SpanData> withResource = spans.stream()
                .map(s -> new DelegatingSpanData(s) {
                    @Override
                    public Resource getResource() {
                        return s.getResource().merge(added);
                    }
                })
                .collect(Collectors.toList());
        return delegate.export(withResource);
    }

    @Override
    public CompletableResultCode flush() {
        return delegate.flush();
    }

    @Override
    public CompletableResultCode shutdown() {
        return delegate.shutdown();
    }
}
