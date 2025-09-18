package com.judgmentlabs.judgeval.tracer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.data.Example;
import com.judgmentlabs.judgeval.data.ExampleEvaluationRun;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameRequest;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameResponse;
import com.judgmentlabs.judgeval.scorers.BaseScorer;
import com.judgmentlabs.judgeval.tracer.exporters.JudgmentSpanExporter;
import com.judgmentlabs.judgeval.tracer.exporters.NoOpSpanExporter;
import com.judgmentlabs.judgeval.utils.Logger;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.sdk.trace.export.SpanExporter;

/**
 * Main tracer class for integrating with Judgment Labs for distributed tracing and evaluation.
 *
 * <p>The Tracer class provides functionality to:
 *
 * <ul>
 *   <li>Export OpenTelemetry spans to Judgment Labs
 *   <li>Automatically evaluate spans using configured scorers
 *   <li>Set custom attributes on spans for better observability
 *   <li>Integrate with existing OpenTelemetry instrumentation
 * </ul>
 *
 * <h2>Basic Usage</h2>
 *
 * <pre>{@code
 * // Simple usage with defaults
 * Tracer tracer = Tracer.createDefault("my-project");
 *
 * // Get the span exporter for OpenTelemetry integration
 * SpanExporter exporter = tracer.getSpanExporter();
 *
 * // Set custom attributes on the current span
 * tracer.setAttribute("custom.key", "custom.value");
 *
 * // Evaluate a scorer with an example
 * Example example = new Example();
 * example.setAdditionalProperty("input", "user input");
 * example.setAdditionalProperty("output", "model output");
 *
 * BaseScorer scorer = new AnswerRelevancyScorer();
 * tracer.asyncEvaluate(scorer, example, "gpt-4", 1.0);
 * }</pre>
 *
 * <h2>Advanced Configuration</h2>
 *
 * <pre>{@code
 * TracerConfiguration config = TracerConfiguration.builder()
 *         .projectName("my-project")
 *         .apiKey("custom-api-key")
 *         .organizationId("custom-org-id")
 *         .enableEvaluation(false)
 *         .build();
 *
 * Tracer tracer = Tracer.createWithConfiguration(config);
 * }</pre>
 *
 * <h2>OpenTelemetry Integration</h2>
 *
 * <pre>{@code
 * Tracer tracer = Tracer.createDefault("my-project");
 * SpanExporter exporter = tracer.getSpanExporter();
 *
 * SdkTracerProvider provider = SdkTracerProvider.builder()
 *         .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
 *         .build();
 *
 * OpenTelemetrySdk.builder()
 *         .setTracerProvider(provider)
 *         .buildAndRegisterGlobal();
 * }</pre>
 *
 * @see TracerConfiguration
 * @see SpanExporter
 * @see BaseScorer
 * @see Example
 */
public final class Tracer {
    private final TracerConfiguration configuration;
    private final JudgmentSyncClient apiClient;
    private final ISerializer serializer;
    private final String projectId;

    private Tracer(
            TracerConfiguration configuration,
            JudgmentSyncClient apiClient,
            ISerializer serializer) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.apiClient = Objects.requireNonNull(apiClient, "API client cannot be null");
        this.serializer = Objects.requireNonNull(serializer, "Serializer cannot be null");
        this.projectId = resolveProjectId(configuration.projectName());
        if (this.projectId == null) {
            Logger.error(
                    "Failed to resolve project "
                            + configuration.projectName()
                            + ", please create it first at https://app.judgmentlabs.ai/org/"
                            + configuration.organizationId()
                            + "/projects. Skipping Judgment export.");
        }
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

    /**
     * Gets the SpanExporter for integration with OpenTelemetry.
     *
     * <p>If the project ID cannot be resolved (e.g., the project doesn't exist), this method
     * returns a NoOpSpanExporter.
     *
     * <p>Example usage:
     *
     * <pre>{@code
     * Tracer tracer = Tracer.createDefault("my-project");
     * SpanExporter exporter = tracer.getSpanExporter();
     *
     * SdkTracerProvider provider = SdkTracerProvider.builder()
     *         .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
     *         .build();
     * }</pre>
     *
     * @return a SpanExporter that sends spans to Judgment Labs, or a NoOpSpanExporter if project
     *     resolution fails
     */
    public SpanExporter getSpanExporter() {
        if (projectId == null) {
            Logger.error(
                    "Project not resolved; cannot create exporter, returning NoOpSpanExporter");
            return new NoOpSpanExporter();
        }
        return createJudgmentSpanExporter(projectId);
    }

    /**
     * Sets the span kind attribute on the current span.
     *
     * <p>Common span kinds include:
     *
     * <ul>
     *   <li>"span" - General application spans
     *   <li>"llm" - Language model interactions
     *   <li>"tool" - External tool or API calls
     * </ul>
     *
     * @param kind the span kind to set (if null, no attribute is set)
     */
    public void setSpanKind(String kind) {
        Optional.ofNullable(Span.current())
                .filter(span -> kind != null)
                .ifPresent(
                        span ->
                                span.setAttribute(
                                        OpenTelemetryKeys.AttributeKeys.JUDGMENT_SPAN_KIND, kind));
    }

    /**
     * Sets a custom attribute on the current span.
     *
     * <p>This method serializes the value to JSON and sets it as a string attribute on the current
     * span. This is useful for adding custom metadata to spans for better observability.
     *
     * <p>If there is no current span, this method does nothing.
     *
     * @param key the attribute key (must not be null)
     * @param value the attribute value (will be serialized to JSON)
     */
    public void setAttribute(String key, Object value) {
        Optional.ofNullable(Span.current())
                .ifPresent(span -> span.setAttribute(key, serializer.serialize(value)));
    }

    /**
     * Sets a custom attribute on the current span with a specific type.
     *
     * <p>This method is similar to {@link #setAttribute(String, Object)} but allows you to specify
     * the type for JSON serialization. This is useful when you need to preserve type information or
     * when dealing with generic types.
     *
     * <p>If there is no current span, this method does nothing.
     *
     * @param key the attribute key (must not be null)
     * @param value the attribute value (will be serialized to JSON)
     * @param type the type to use for JSON serialization (must not be null)
     */
    public void setAttribute(String key, Object value, Type type) {
        Optional.ofNullable(Span.current())
                .ifPresent(span -> span.setAttribute(key, serializer.serialize(value, type)));
    }

    /**
     * Asynchronously evaluates a scorer with an example.
     *
     * <p>The evaluation includes:
     *
     * <ul>
     *   <li>The current trace and span context for correlation
     *   <li>The scorer configuration and threshold
     *   <li>The example input and output data
     *   <li>The model used for generation
     * </ul>
     *
     * <p>If evaluation is disabled in the configuration, this method does nothing. The method
     * respects OpenTelemetry's internal sampler - evaluation only runs if the current span is
     * recording.
     *
     * @param scorer the scorer to use for evaluation (must not be null)
     * @param example the example to evaluate (must not be null)
     * @param model the model used for generation (can be null, will use default)
     */
    public void asyncEvaluate(BaseScorer scorer, Example example, String model) {
        try {
            if (!configuration.enableEvaluation()) {
                return;
            }

            Span currentSpan = Span.current();
            if (currentSpan == null || !currentSpan.getSpanContext().isSampled()) {
                return;
            }

            SpanContext spanContext = currentSpan.getSpanContext();
            String traceId = spanContext.getTraceId();
            String spanId = spanContext.getSpanId();

            Object transport = scorer.toTransport();
            Logger.info(
                    "asyncEvaluate: project="
                            + configuration.projectName()
                            + ", traceId="
                            + traceId
                            + ", spanId="
                            + spanId
                            + ", scorerTransport="
                            + (transport != null ? transport.getClass().getSimpleName() : "null"));

            ExampleEvaluationRun evaluationRun =
                    createEvaluationRun(scorer, example, model, traceId, spanId);
            enqueueEvaluation(evaluationRun);
        } catch (Exception e) {
            Logger.error("Failed to evaluate scorer: " + e.getMessage());
        }
    }

    public void asyncEvaluate(BaseScorer scorer, Example example) {
        asyncEvaluate(scorer, example, null);
    }

    public void setAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            return;
        }
        Optional.ofNullable(Span.current())
                .ifPresent(
                        span ->
                                attributes.forEach(
                                        (key, value) ->
                                                span.setAttribute(
                                                        key, serializer.serialize(value))));
    }

    public void setLLMSpan() {
        setSpanKind("llm");
    }

    public void setToolSpan() {
        setSpanKind("tool");
    }

    public void setGeneralSpan() {
        setSpanKind("span");
    }

    public void setInput(Object input) {
        setAttribute(OpenTelemetryKeys.AttributeKeys.JUDGMENT_INPUT, input);
    }

    public void setOutput(Object output) {
        setAttribute(OpenTelemetryKeys.AttributeKeys.JUDGMENT_OUTPUT, output);
    }

    public void setInput(Object input, Type type) {
        setAttribute(OpenTelemetryKeys.AttributeKeys.JUDGMENT_INPUT, input, type);
    }

    public void setOutput(Object output, Type type) {
        setAttribute(OpenTelemetryKeys.AttributeKeys.JUDGMENT_OUTPUT, output, type);
    }

    private String resolveProjectId(String name) {
        try {
            ResolveProjectNameRequest request = new ResolveProjectNameRequest();
            request.setProjectName(name);
            ResolveProjectNameResponse response = apiClient.projectsResolve(request);
            return Optional.ofNullable(response.getProjectId()).map(Object::toString).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private JudgmentSpanExporter createJudgmentSpanExporter(String projectId) {
        String endpoint =
                configuration.apiUrl().endsWith("/")
                        ? configuration.apiUrl() + "otel/v1/traces"
                        : configuration.apiUrl() + "/otel/v1/traces";
        return JudgmentSpanExporter.builder()
                .endpoint(endpoint)
                .apiKey(configuration.apiKey())
                .organizationId(configuration.organizationId())
                .projectId(projectId)
                .build();
    }

    private ExampleEvaluationRun createEvaluationRun(
            BaseScorer scorer, Example example, String model, String traceId, String spanId) {
        String runId = "async_evaluate_" + (spanId != null ? spanId : System.currentTimeMillis());
        String modelName = model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL;

        ExampleEvaluationRun evaluationRun =
                new ExampleEvaluationRun(
                        configuration.projectName(),
                        runId,
                        List.of(example),
                        List.of(scorer.toTransport()),
                        modelName,
                        configuration.organizationId());
        evaluationRun.setTraceId(traceId);
        evaluationRun.setTraceSpanId(spanId);
        return evaluationRun;
    }

    private void enqueueEvaluation(ExampleEvaluationRun evaluationRun) {
        try {
            apiClient.addToRunEvalQueue(evaluationRun);
        } catch (Exception e) {
            Logger.error("Failed to enqueue evaluation run: " + e.getMessage());
        }
    }

    /**
     * Builder for creating Tracer instances with custom dependencies.
     *
     * <p>Example usage:
     *
     * <pre>{@code
     * TracerConfiguration config = TracerConfiguration.builder()
     *         .projectName("my-project")
     *         .build();
     *
     * JudgmentSyncClient customClient = new JudgmentSyncClient(url, key, orgId);
     * ISerializer customSerializer = obj -> obj.toString();
     *
     * Tracer tracer = Tracer.builder()
     *         .configuration(config)
     *         .apiClient(customClient)
     *         .serializer(customSerializer)
     *         .build();
     * }</pre>
     */
    public static final class TracerBuilder {
        private TracerConfiguration configuration;
        private JudgmentSyncClient apiClient;
        private ISerializer serializer = new GsonSerializer();

        public TracerBuilder configuration(TracerConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public TracerBuilder apiClient(JudgmentSyncClient apiClient) {
            this.apiClient = apiClient;
            return this;
        }

        public TracerBuilder serializer(ISerializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public Tracer build() {
            if (configuration == null) {
                throw new IllegalArgumentException("Configuration is required");
            }

            JudgmentSyncClient client =
                    apiClient != null
                            ? apiClient
                            : new JudgmentSyncClient(
                                    configuration.apiUrl(),
                                    configuration.apiKey(),
                                    configuration.organizationId());

            return new Tracer(configuration, client, serializer);
        }
    }

    private static class GsonSerializer implements ISerializer {
        private final Gson gson = new Gson();

        @Override
        public String serialize(Object obj) {
            return gson.toJson(obj);
        }

        @Override
        public String serialize(Object obj, Type type) {
            return gson.toJson(obj, type);
        }
    }
}
