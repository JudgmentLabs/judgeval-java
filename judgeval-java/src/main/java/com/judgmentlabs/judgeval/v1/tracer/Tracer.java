package com.judgmentlabs.judgeval.v1.tracer;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.Version;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.tracer.ISerializer;
import com.judgmentlabs.judgeval.tracer.TracerConfiguration;
import com.judgmentlabs.judgeval.utils.Logger;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public final class Tracer extends BaseTracer {
    private SdkTracerProvider tracerProvider;

    private Tracer(Builder builder) {
        super(
                buildConfiguration(builder),
                new JudgmentSyncClient(
                        builder.apiUrl != null ? builder.apiUrl : Env.JUDGMENT_API_URL,
                        Objects.requireNonNull(builder.apiKey, "apiKey required"),
                        Objects.requireNonNull(builder.organizationId, "organizationId required")),
                builder.serializer != null ? builder.serializer : new GsonSerializer());

        if (builder.initialize) {
            initialize();
        }
    }

    private static TracerConfiguration buildConfiguration(Builder builder) {
        return TracerConfiguration.builder()
                .projectName(Objects.requireNonNull(builder.projectName, "projectName required"))
                .apiKey(Objects.requireNonNull(builder.apiKey, "apiKey required"))
                .organizationId(Objects.requireNonNull(builder.organizationId, "organizationId required"))
                .apiUrl(builder.apiUrl != null ? builder.apiUrl : Env.JUDGMENT_API_URL)
                .enableEvaluation(builder.enableEvaluation)
                .build();
    }

    @Override
    public void initialize() {
        SpanExporter spanExporter = getSpanExporter();

        var resource = Resource.getDefault()
                .merge(Resource.create(Attributes.builder()
                        .put("service.name", configuration.projectName())
                        .put("telemetry.sdk.name", TRACER_NAME)
                        .put("telemetry.sdk.version", Version.getVersion())
                        .build()));

        this.tracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter)
                        .build())
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(this.tracerProvider)
                .build();

        GlobalOpenTelemetry.set(openTelemetry);
    }

    @Override
    public boolean forceFlush(int timeoutMillis) {
        if (tracerProvider == null) {
            Logger.error("Cannot forceFlush: tracer not initialized");
            return false;
        }
        return tracerProvider.forceFlush()
                .join(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
                .isSuccess();
    }

    @Override
    public void shutdown(int timeoutMillis) {
        if (tracerProvider == null) {
            Logger.error("Cannot shutdown: tracer not initialized");
            return;
        }
        tracerProvider.shutdown()
                .join(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String projectName;
        private String apiKey;
        private String organizationId;
        private String apiUrl;
        private boolean enableEvaluation = true;
        private ISerializer serializer;
        private boolean initialize = false;

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        public Builder enableEvaluation(boolean enableEvaluation) {
            this.enableEvaluation = enableEvaluation;
            return this;
        }

        public Builder serializer(ISerializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder initialize(boolean initialize) {
            this.initialize = initialize;
            return this;
        }

        public Tracer build() {
            return new Tracer(this);
        }
    }

    private static class GsonSerializer implements ISerializer {
        private final Gson gson = new Gson();

        @Override
        public String serialize(Object obj) {
            return Optional.ofNullable(obj)
                    .map(o -> serialize(o, o.getClass()))
                    .orElse(null);
        }

        @Override
        public String serialize(Object obj, Type type) {
            try {
                return gson.toJson(obj, type);
            } catch (Exception e) {
                Logger.error("Failed to serialize object: " + e.getMessage());
                return Optional.ofNullable(obj)
                        .map(Object::toString)
                        .orElse(null);
            }
        }
    }
}
