package com.judgmentlabs.judgeval.tracer;

import java.util.ArrayList;
import java.util.List;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.api.models.ResolveProjectNameRequest;
import com.judgmentlabs.judgeval.api.models.ResolveProjectNameResponse;
import com.judgmentlabs.judgeval.data.EvaluationRun;
import com.judgmentlabs.judgeval.data.Example;
import com.judgmentlabs.judgeval.scorers.BaseScorer;
import com.judgmentlabs.judgeval.tracer.exporters.JudgmentSpanExporter;
import com.judgmentlabs.judgeval.tracer.exporters.NoOpSpanExporter;
import com.judgmentlabs.judgeval.utils.Logger;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public class Tracer {
    private final String apiKey;
    private final String organizationId;
    private final String projectName;
    private final boolean enableEvaluation;
    private final JudgmentSyncClient apiClient;
    private final String projectId;

    public Tracer(String projectName) {
        this(projectName, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID, true);
    }

    public Tracer(
            String projectName, String apiKey, String organizationId, boolean enableEvaluation) {
        this.projectName = projectName;
        this.apiKey = apiKey;
        this.organizationId = organizationId;
        this.enableEvaluation = enableEvaluation;
        this.apiClient = new JudgmentSyncClient(Env.JUDGMENT_API_URL, this.apiKey, this.organizationId);
        this.projectId = resolveProjectId(projectName);
        if (this.projectId == null) {
            Logger.warning(
                    "Failed to resolve project "
                            + projectName
                            + ", please create it first at https://app.judgmentlabs.ai/projects. Skipping Judgment export.");
        }
    }

    public SpanExporter getSpanExporter() {
        if (projectId == null) {
            Logger.error("Project not resolved; cannot create exporter, returning NoOpSpanExporter");
            return new NoOpSpanExporter();
        }
        String endpoint = Env.JUDGMENT_API_URL.endsWith("/")
                ? Env.JUDGMENT_API_URL + "otel/v1/traces"
                : Env.JUDGMENT_API_URL + "/otel/v1/traces";
        return new JudgmentSpanExporter(endpoint, apiKey, organizationId, projectId);
    }

    private String resolveProjectId(String name) {
        try {
            ResolveProjectNameRequest req = new ResolveProjectNameRequest();
            req.setProjectName(name);
            ResolveProjectNameResponse resp = apiClient.projectsResolve(req);
            Object id = resp.getProjectId();
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            Logger.error("Failed to resolve project ID for project '" + name + "': " + e.getMessage());
            return null;
        }
    }

    public void asyncEvaluate(
            BaseScorer scorer, Example example, String model, double samplingRate) {
        if (!enableEvaluation) {
            Logger.info("Skipping evaluation because enableEvaluation is false");
            return;
        }
        Span span = Span.current();
        SpanContext ctx = span.getSpanContext();
        String traceId = ctx != null ? ctx.getTraceId() : null;
        String spanId = ctx != null ? ctx.getSpanId() : null;

        Object transport = scorer.toTransport();
        Logger.info(
                "asyncEvaluate: project="
                        + projectName
                        + ", traceId="
                        + traceId
                        + ", spanId="
                        + spanId
                        + ", scorerTransport="
                        + (transport != null ? transport.getClass().getSimpleName() : "null"));

        List<Object> scorers = new ArrayList<>();
        scorers.add(transport);

        EvaluationRun eval = new EvaluationRun(
                projectName,
                "async_evaluate_" + (spanId != null ? spanId : System.currentTimeMillis()),
                List.of(example),
                scorers,
                model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL,
                organizationId);
        eval.setTraceId(traceId);
        eval.setTraceSpanId(spanId);
        try {
            apiClient.addToRunEvalQueue(eval);
        } catch (Exception e) {
            Logger.error("Failed to enqueue evaluation run: " + e.getMessage());
        }
    }
}
