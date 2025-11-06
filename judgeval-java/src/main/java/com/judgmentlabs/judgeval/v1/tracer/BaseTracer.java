package com.judgmentlabs.judgeval.v1.tracer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.JudgmentAttributeKeys;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.ExampleEvaluationRun;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameRequest;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameResponse;
import com.judgmentlabs.judgeval.internal.api.models.TraceEvaluationRun;
import com.judgmentlabs.judgeval.utils.Logger;
import com.judgmentlabs.judgeval.v1.data.Example;
import com.judgmentlabs.judgeval.v1.scorers.BaseScorer;
import com.judgmentlabs.judgeval.v1.tracer.exporters.JudgmentSpanExporter;
import com.judgmentlabs.judgeval.v1.tracer.exporters.NoOpSpanExporter;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public abstract class BaseTracer {
    public static final String         TRACER_NAME = "judgeval";

    protected final String             projectName;
    protected final String             apiKey;
    protected final String             organizationId;
    protected final String             apiUrl;
    protected final boolean            enableEvaluation;
    protected final JudgmentSyncClient apiClient;
    protected final ISerializer        serializer;
    protected final ObjectMapper       jacksonMapper;
    protected final Optional<String>   projectId;

    protected BaseTracer(String projectName, String apiKey, String organizationId, String apiUrl,
            boolean enableEvaluation, JudgmentSyncClient apiClient, ISerializer serializer) {
        this.projectName = Objects.requireNonNull(projectName, "projectName required");
        this.apiKey = Objects.requireNonNull(apiKey, "apiKey required");
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId required");
        this.apiUrl = Objects.requireNonNull(apiUrl, "apiUrl required");
        this.enableEvaluation = enableEvaluation;
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient required");
        this.serializer = Objects.requireNonNull(serializer, "serializer required");
        this.jacksonMapper = new ObjectMapper();
        this.projectId = resolveProjectId(projectName);

        this.projectId.ifPresentOrElse(id -> {
        }, () -> Logger.error("Failed to resolve project " + projectName
                + ", please create it first at https://app.judgmentlabs.ai/org/" + organizationId
                + "/projects. Skipping Judgment export."));
    }

    public abstract void initialize();

    public abstract boolean forceFlush(int timeoutMillis);

    public abstract void shutdown(int timeoutMillis);

    public SpanExporter getSpanExporter() {
        return projectId.<SpanExporter>map(this::createJudgmentSpanExporter)
                .orElseGet(() -> {
                    Logger.error("Project not resolved; cannot create exporter, returning NoOpSpanExporter");
                    return new NoOpSpanExporter();
                });
    }

    public void setSpanKind(String kind) {
        Optional.ofNullable(kind)
                .ifPresent(k -> withCurrentSpan(
                        span -> span.setAttribute(JudgmentAttributeKeys.AttributeKeys.JUDGMENT_SPAN_KIND, k)));
    }

    private static void withCurrentSpan(java.util.function.Consumer<Span> action) {
        Optional.ofNullable(Span.current())
                .ifPresent(action);
    }

    private static boolean isValidKey(String key) {
        return key != null && !key.isEmpty();
    }

    public void setAttribute(String key, Object value) {
        if (!isValidKey(key)) {
            return;
        }
        if (value != null) {
            setAttribute(key, value, value.getClass());
        }
    }

    public void setAttribute(String key, Object value, Type type) {
        if (!isValidKey(key)) {
            return;
        }
        if (value != null) {
            withCurrentSpan(span -> span.setAttribute(key, serializer.serialize(value, type)));
        }
    }

    public void setAttribute(String key, String value) {
        if (!isValidKey(key)) {
            return;
        }
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    public void setAttribute(String key, long value) {
        if (!isValidKey(key)) {
            return;
        }
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    public void setAttribute(String key, double value) {
        if (!isValidKey(key)) {
            return;
        }
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    public void setAttribute(String key, boolean value) {
        if (!isValidKey(key)) {
            return;
        }
        withCurrentSpan(span -> span.setAttribute(key, value));
    }

    private Optional<SpanContext> getSampledSpanContext() {
        return Optional.ofNullable(Span.current())
                .filter(span -> span.getSpanContext()
                        .isSampled())
                .map(Span::getSpanContext);
    }

    private Optional<Span> getSampledSpan() {
        return Optional.ofNullable(Span.current())
                .filter(span -> span.getSpanContext()
                        .isSampled());
    }

    private boolean isEvaluationEnabled() {
        return enableEvaluation;
    }

    private void logEvaluationInfo(String method, String traceId, String spanId, String scorerName) {
        Logger.info(method + ": project=" + projectName + ", traceId=" + traceId + ", spanId="
                + spanId + ", scorer=" + scorerName);
    }

    private void safeExecute(String operation, Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            Logger.error("Failed to " + operation + ": " + e.getMessage());
        }
    }

    public void asyncEvaluate(BaseScorer scorer, Example example, String model) {
        safeExecute("evaluate scorer", () -> {
            if (!isEvaluationEnabled()) {
                return;
            }

            getSampledSpanContext().ifPresent(spanContext -> {
                String traceId = spanContext.getTraceId();
                String spanId = spanContext.getSpanId();

                logEvaluationInfo("asyncEvaluate", traceId, spanId, scorer.getName());

                ExampleEvaluationRun evaluationRun = createEvaluationRun(scorer, example, model, traceId, spanId);
                enqueueEvaluation(evaluationRun);
            });
        });
    }

    public void asyncEvaluate(BaseScorer scorer, Example example) {
        asyncEvaluate(scorer, example, null);
    }

    public void asyncTraceEvaluate(BaseScorer scorer, String model) {
        safeExecute("evaluate trace scorer", () -> {
            if (!isEvaluationEnabled()) {
                return;
            }

            getSampledSpan().ifPresent(currentSpan -> {
                SpanContext spanContext = currentSpan.getSpanContext();
                String traceId = spanContext.getTraceId();
                String spanId = spanContext.getSpanId();

                logEvaluationInfo("asyncTraceEvaluate", traceId, spanId, scorer.getName());

                TraceEvaluationRun evaluationRun = createTraceEvaluationRun(scorer, model, traceId, spanId);
                try {
                    String traceEvalJson = jacksonMapper.writeValueAsString(evaluationRun);
                    currentSpan.setAttribute(JudgmentAttributeKeys.AttributeKeys.JUDGMENT_PENDING_TRACE_EVAL,
                            traceEvalJson);
                } catch (Exception e) {
                    Logger.error("Failed to serialize trace evaluation: " + e.getMessage());
                }
            });
        });
    }

    public void asyncTraceEvaluate(BaseScorer scorer) {
        asyncTraceEvaluate(scorer, null);
    }

    public void setAttributes(Map<String, Object> attributes) {
        Optional.ofNullable(attributes)
                .ifPresent(attrs -> attrs.forEach(this::setAttribute));
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
        setAttribute(JudgmentAttributeKeys.AttributeKeys.JUDGMENT_INPUT, input);
    }

    public void setOutput(Object output) {
        setAttribute(JudgmentAttributeKeys.AttributeKeys.JUDGMENT_OUTPUT, output);
    }

    public void setInput(Object input, Type type) {
        setAttribute(JudgmentAttributeKeys.AttributeKeys.JUDGMENT_INPUT, input, type);
    }

    public void setOutput(Object output, Type type) {
        setAttribute(JudgmentAttributeKeys.AttributeKeys.JUDGMENT_OUTPUT, output, type);
    }

    public void span(String spanName, Runnable runnable) {
        Span span = getTracer().spanBuilder(spanName)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            runnable.run();
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR).recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    public <T> T span(String spanName, java.util.concurrent.Callable<T> callable) throws Exception {
        Span span = getTracer().spanBuilder(spanName)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            return callable.call();
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR).recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    public io.opentelemetry.api.trace.Tracer getTracer() {
        return GlobalOpenTelemetry.get()
                .getTracer(TRACER_NAME);
    }

    public String getProjectName() {
        return projectName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public boolean isEnableEvaluation() {
        return enableEvaluation;
    }

    public Optional<String> getProjectId() {
        return projectId;
    }

    public static Span span(String spanName) {
        return GlobalOpenTelemetry.get()
                .getTracer(TRACER_NAME)
                .spanBuilder(spanName)
                .startSpan();
    }

    private Optional<String> resolveProjectId(String name) {
        try {
            ResolveProjectNameRequest request = new ResolveProjectNameRequest();
            request.setProjectName(name);
            ResolveProjectNameResponse response = apiClient.projectsResolve(request);
            return Optional.ofNullable(response.getProjectId())
                    .map(Object::toString);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String buildEndpoint(String baseUrl) {
        return baseUrl.endsWith("/") ? baseUrl + "otel/v1/traces" : baseUrl + "/otel/v1/traces";
    }

    private JudgmentSpanExporter createJudgmentSpanExporter(String projectId) {
        return JudgmentSpanExporter.builder()
                .endpoint(buildEndpoint(apiUrl))
                .apiKey(apiKey)
                .organizationId(organizationId)
                .projectId(projectId)
                .build();
    }

    private String generateRunId(String prefix, String spanId) {
        return prefix + Optional.ofNullable(spanId)
                .orElseGet(() -> String.valueOf(System.currentTimeMillis()));
    }

    private ExampleEvaluationRun createEvaluationRun(BaseScorer scorer, Example example, String model, String traceId,
            String spanId) {
        String runId = generateRunId("async_evaluate_", spanId);
        String modelName = model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL;

        ExampleEvaluationRun evaluationRun = new ExampleEvaluationRun();
        evaluationRun.setProjectName(projectName);
        evaluationRun.setEvalName(runId);
        evaluationRun.setModel(modelName);
        evaluationRun.setTraceId(traceId);
        evaluationRun.setTraceSpanId(spanId);

        List<com.judgmentlabs.judgeval.internal.api.models.Example> examples = new ArrayList<>();
        examples.add(example);
        evaluationRun.setExamples(examples);

        evaluationRun.setCustomScorers(List.of());
        evaluationRun.setJudgmentScorers(List.of(scorer.getScorerConfig()));

        return evaluationRun;
    }

    private TraceEvaluationRun createTraceEvaluationRun(BaseScorer scorer, String model, String traceId,
            String spanId) {
        String evalName = generateRunId("async_trace_evaluate_", spanId);
        String modelName = model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL;

        TraceEvaluationRun evaluationRun = new TraceEvaluationRun();
        evaluationRun.setProjectName(projectName);
        evaluationRun.setEvalName(evalName);
        evaluationRun.setModel(modelName);
        evaluationRun.setTraceAndSpanIds(List.of(List.of(traceId, spanId)));
        evaluationRun.setJudgmentScorers(List.of(scorer.getScorerConfig()));

        return evaluationRun;
    }

    private void enqueueEvaluation(ExampleEvaluationRun evaluationRun) {
        try {
            apiClient.addToRunEvalQueue(evaluationRun);
        } catch (Exception e) {
            Logger.error("Failed to enqueue evaluation run: " + e.getMessage());
        }
    }
}
