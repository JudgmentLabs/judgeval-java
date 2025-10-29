package com.judgmentlabs.judgeval.tracer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.data.Example;
import com.judgmentlabs.judgeval.data.ExampleEvaluationRun;
import com.judgmentlabs.judgeval.data.TraceEvaluationRun;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameRequest;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameResponse;
import com.judgmentlabs.judgeval.scorers.BaseScorer;
import com.judgmentlabs.judgeval.tracer.exporters.JudgmentSpanExporter;
import com.judgmentlabs.judgeval.tracer.exporters.NoOpSpanExporter;
import com.judgmentlabs.judgeval.utils.Logger;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public abstract class BaseTracer {
    public static final String TRACER_NAME = "judgeval";

    protected final TracerConfiguration configuration;
    protected final JudgmentSyncClient apiClient;
    protected final ISerializer serializer;
    protected final ObjectMapper jacksonMapper;
    protected final String projectId;

    protected BaseTracer(
            TracerConfiguration configuration, ISerializer serializer, boolean initialize) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.apiClient =
                new JudgmentSyncClient(
                        configuration.apiUrl(),
                        configuration.apiKey(),
                        configuration.organizationId());
        this.serializer = Objects.requireNonNull(serializer, "Serializer cannot be null");
        this.jacksonMapper = new ObjectMapper();
        this.projectId = resolveProjectId(configuration.projectName());
        if (this.projectId == null) {
            Logger.error(
                    "Failed to resolve project "
                            + configuration.projectName()
                            + ", please create it first at https://app.judgmentlabs.ai/org/"
                            + configuration.organizationId()
                            + "/projects. Skipping Judgment export.");
        }

        if (initialize) {
            initialize();
        }
    }

    public abstract void initialize();

    /**
     * Gets the SpanExporter for OpenTelemetry integration. Returns NoOpSpanExporter if project ID
     * cannot be resolved.
     */
    public SpanExporter getSpanExporter() {
        if (projectId == null) {
            Logger.error(
                    "Project not resolved; cannot create exporter, returning NoOpSpanExporter");
            return new NoOpSpanExporter();
        }
        return createJudgmentSpanExporter(projectId);
    }

    public void setSpanKind(String kind) {
        if (kind == null) {
            return;
        }
        withCurrentSpan(
                span ->
                        span.setAttribute(
                                OpenTelemetryKeys.AttributeKeys.JUDGMENT_SPAN_KIND, kind));
    }

    private static void withCurrentSpan(java.util.function.Consumer<Span> action) {
        Span span = Span.current();
        if (span != null) {
            action.accept(span);
        }
    }

    public void setAttribute(String key, Object value) {
        if (value == null) {
            return;
        }
        setAttribute(key, value, value.getClass());
    }

    public void setAttribute(String key, Object value, Type type) {
        if (value == null) {
            return;
        }
        withCurrentSpan(span -> span.setAttribute(key, serializer.serialize(value, type)));
    }

    public void setAttribute(String key, String value) {
        if (value == null) {
            return;
        }
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    public void setAttribute(String key, long value) {
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    public void setAttribute(String key, double value) {
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    public void setAttribute(String key, boolean value) {
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    /** Asynchronously evaluates a scorer with an example using current trace context. */
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

            Logger.info(
                    "asyncEvaluate: project="
                            + configuration.projectName()
                            + ", traceId="
                            + traceId
                            + ", spanId="
                            + spanId
                            + ", scorer="
                            + scorer.getName());

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

    /** Asynchronously evaluates a scorer with trace context. Sets evaluation as span attribute. */
    public void asyncTraceEvaluate(BaseScorer scorer, String model) {
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

            Logger.info(
                    "asyncTraceEvaluate: project="
                            + configuration.projectName()
                            + ", traceId="
                            + traceId
                            + ", spanId="
                            + spanId
                            + ", scorer="
                            + scorer.getName());

            TraceEvaluationRun evaluationRun =
                    createTraceEvaluationRun(scorer, model, traceId, spanId);
            try {
                String traceEvalJson = jacksonMapper.writeValueAsString(evaluationRun);
                currentSpan.setAttribute(
                        OpenTelemetryKeys.AttributeKeys.PENDING_TRACE_EVAL, traceEvalJson);
            } catch (Exception e) {
                Logger.error("Failed to serialize trace evaluation: " + e.getMessage());
            }
        } catch (Exception e) {
            Logger.error("Failed to evaluate trace scorer: " + e.getMessage());
        }
    }

    public void asyncTraceEvaluate(BaseScorer scorer) {
        asyncTraceEvaluate(scorer, null);
    }

    public void setAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            return;
        }
        withCurrentSpan(span -> attributes.forEach(this::setAttribute));
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

    /**
     * Creates a new span with the given name and executes the provided runnable within its scope.
     * The span is automatically ended when the runnable completes.
     */
    public void span(String spanName, Runnable runnable) {
        Span span = getTracer().spanBuilder(spanName).startSpan();
        try (Scope scope = span.makeCurrent()) {
            runnable.run();
        } finally {
            span.end();
        }
    }

    /**
     * Creates a new span with the given name and executes the provided callable within its scope.
     * The span is automatically ended when the callable completes. Returns the result of the
     * callable.
     */
    public <T> T span(String spanName, java.util.concurrent.Callable<T> callable) throws Exception {
        Span span = getTracer().spanBuilder(spanName).startSpan();
        try (Scope scope = span.makeCurrent()) {
            return callable.call();
        } finally {
            span.end();
        }
    }

    /** Gets the OpenTelemetry tracer instance. */
    public Tracer getTracer() {
        return GlobalOpenTelemetry.get().getTracer(TRACER_NAME);
    }

    /**
     * Creates a new span with the given name. The caller is responsible for ending the span
     * manually.
     */
    public static Span span(String spanName) {
        return GlobalOpenTelemetry.get().getTracer(TRACER_NAME).spanBuilder(spanName).startSpan();
    }

    protected String resolveProjectId(String name) {
        try {
            ResolveProjectNameRequest request = new ResolveProjectNameRequest();
            request.setProjectName(name);
            ResolveProjectNameResponse response = apiClient.projectsResolve(request);
            Object projectId = response.getProjectId();
            return projectId != null ? projectId.toString() : null;
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
                        List.of(scorer.getScorerConfig()),
                        modelName,
                        configuration.organizationId());
        evaluationRun.setTraceId(traceId);
        evaluationRun.setTraceSpanId(spanId);
        return evaluationRun;
    }

    private TraceEvaluationRun createTraceEvaluationRun(
            BaseScorer scorer, String model, String traceId, String spanId) {
        String evalName =
                "async_trace_evaluate_" + (spanId != null ? spanId : System.currentTimeMillis());
        String modelName = model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL;

        return TraceEvaluationRun.builder()
                .projectName(configuration.projectName())
                .evalName(evalName)
                .scorer(scorer.getScorerConfig())
                .model(modelName)
                .organizationId(configuration.organizationId())
                .traceAndSpanId(traceId, spanId)
                .build();
    }

    private void enqueueEvaluation(ExampleEvaluationRun evaluationRun) {
        try {
            apiClient.addToRunEvalQueue(evaluationRun);
        } catch (Exception e) {
            Logger.error("Failed to enqueue evaluation run: " + e.getMessage());
        }
    }

    protected static class GsonSerializer implements ISerializer {
        private final Gson gson = new Gson();

        @Override
        public String serialize(Object obj) {
            return serialize(obj, obj.getClass());
        }

        @Override
        public String serialize(Object obj, Type type) {
            try {
                return gson.toJson(obj, type);
            } catch (Exception e) {
                Logger.error("Failed to serialize object: " + e.getMessage());
                return obj != null ? obj.toString() : null;
            }
        }
    }
}
