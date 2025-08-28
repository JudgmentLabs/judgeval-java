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

/**
 * SpanExporter implementation that sends spans to Judgment Labs with project
 * identification.
 * 
 * <p>
 * This exporter wraps the OTLP HTTP exporter and adds Judgment Labs specific
 * headers
 * and project identification to all exported spans.
 * </p>
 */
public class JudgmentSpanExporter implements SpanExporter {
    private final SpanExporter delegate;
    private final String projectId;

    /**
     * Creates a new JudgmentSpanExporter with the specified configuration.
     * 
     * @param endpoint       the OTLP endpoint URL
     * @param apiKey         the API key for authentication
     * @param organizationId the organization ID
     * @param projectId      the project ID (must not be null or empty)
     * @throws IllegalArgumentException if projectId is null or empty
     */
    public JudgmentSpanExporter(
            String endpoint, String apiKey, String organizationId, String projectId) {
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

    /**
     * Creates a new builder for constructing JudgmentSpanExporter instances.
     * 
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        Logger.info(
                "JudgmentSpanExporter exporting spans: count="
                        + (spans != null ? spans.size() : 0));
        AttributeKey<String> key = AttributeKey.stringKey(OpenTelemetryKeys.ResourceKeys.JUDGMENT_PROJECT_ID);
        Attributes extra = Attributes.of(key, projectId);
        Resource added = Resource.create(extra);
        List<SpanData> withResource = spans.stream()
                .map(
                        s -> new DelegatingSpanData(s) {
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

    /**
     * Builder for creating JudgmentSpanExporter instances.
     */
    public static final class Builder {
        private String endpoint;
        private String apiKey;
        private String organizationId;
        private String projectId;

        private Builder() {
        }

        /**
         * Sets the OTLP endpoint URL.
         * 
         * @param endpoint the endpoint URL
         * @return this builder for method chaining
         */
        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        /**
         * Sets the API key for authentication.
         * 
         * @param apiKey the API key
         * @return this builder for method chaining
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the organization ID.
         * 
         * @param organizationId the organization ID
         * @return this builder for method chaining
         */
        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        /**
         * Sets the project ID.
         * 
         * @param projectId the project ID
         * @return this builder for method chaining
         */
        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        /**
         * Builds a new JudgmentSpanExporter with the current configuration.
         * 
         * @return a new JudgmentSpanExporter instance
         * @throws IllegalArgumentException if required fields are missing
         */
        public JudgmentSpanExporter build() {
            if (endpoint == null || endpoint.trim().isEmpty()) {
                throw new IllegalArgumentException("Endpoint is required");
            }
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalArgumentException("API key is required");
            }
            if (organizationId == null || organizationId.trim().isEmpty()) {
                throw new IllegalArgumentException("Organization ID is required");
            }
            if (projectId == null || projectId.trim().isEmpty()) {
                throw new IllegalArgumentException("Project ID is required");
            }

            return new JudgmentSpanExporter(endpoint, apiKey, organizationId, projectId);
        }
    }
}
