package com.judgmentlabs.judgeval.tracer;

import com.judgmentlabs.judgeval.Version;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

/**
 * Main tracer for Judgment Labs distributed tracing and evaluation.
 *
 * @see TracerConfiguration
 * @see SpanExporter
 * @see com.judgmentlabs.judgeval.scorers.BaseScorer
 * @see com.judgmentlabs.judgeval.data.Example
 */
public final class Tracer extends BaseTracer {

    private Tracer(
            TracerConfiguration configuration, ISerializer serializer, boolean shouldInitialize) {
        super(configuration, serializer, shouldInitialize);
    }

    public static TracerBuilder builder() {
        return new TracerBuilder();
    }

    public static Tracer createDefault(String projectName) {
        return builder().configuration(TracerConfiguration.createDefault(projectName)).build();
    }

    public static Tracer createWithConfiguration(TracerConfiguration configuration) {
        return builder().configuration(configuration).build();
    }

    /** Initializes the OpenTelemetry SDK with batch span processor and registers it globally. */
    @Override
    public void initialize() {
        SpanExporter spanExporter = getSpanExporter();

        var resource =
                Resource.getDefault()
                        .merge(
                                Resource.create(
                                        Attributes.builder()
                                                .put("service.name", "SqlAgent")
                                                .put("telemetry.sdk.name", TRACER_NAME)
                                                .put("telemetry.sdk.version", Version.getVersion())
                                                .build()));

        SdkTracerProvider tracerProvider =
                SdkTracerProvider.builder()
                        .setResource(resource)
                        .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                        .build();

        OpenTelemetry openTelemetry =
                OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).build();

        GlobalOpenTelemetry.set(openTelemetry);
    }

    /** Builder for creating Tracer instances. */
    public static final class TracerBuilder {
        private TracerConfiguration configuration;
        private ISerializer serializer = new GsonSerializer();
        private boolean initialize = false;

        public TracerBuilder configuration(TracerConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public TracerBuilder serializer(ISerializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public TracerBuilder initialize(boolean initialize) {
            this.initialize = initialize;
            return this;
        }

        public Tracer build() {
            if (configuration == null) {
                throw new IllegalArgumentException("Configuration is required");
            }

            return new Tracer(configuration, serializer, initialize);
        }
    }
}
