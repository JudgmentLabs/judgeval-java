package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleEvaluationRun {
    @JsonProperty("id")
    @Nullable
    private String id;

    @JsonProperty("project_name")
    @Nullable
    private String projectName;

    @JsonProperty("eval_name")
    @Nullable
    private String evalName;

    @JsonProperty("custom_scorers")
    @Nullable
    private List<BaseScorer> customScorers;

    @JsonProperty("judgment_scorers")
    @Nullable
    private List<ScorerConfig> judgmentScorers;

    @JsonProperty("model")
    @Nonnull
    private String model;

    @JsonProperty("created_at")
    @Nullable
    private String createdAt;

    @JsonProperty("examples")
    @Nonnull
    private List<Example> examples;

    @JsonProperty("trace_span_id")
    @Nullable
    private String traceSpanId;

    @JsonProperty("trace_id")
    @Nullable
    private String traceId;

    @Nonnull private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    @Nonnull
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(@Nonnull String name, @Nullable Object value) {
        additionalProperties.put(name, value);
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getProjectName() {
        return projectName;
    }

    @Nullable
    public String getEvalName() {
        return evalName;
    }

    @Nullable
    public List<BaseScorer> getCustomScorers() {
        return customScorers;
    }

    @Nullable
    public List<ScorerConfig> getJudgmentScorers() {
        return judgmentScorers;
    }

    @Nonnull
    public String getModel() {
        return model;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    @Nonnull
    public List<Example> getExamples() {
        return examples;
    }

    @Nullable
    public String getTraceSpanId() {
        return traceSpanId;
    }

    @Nullable
    public String getTraceId() {
        return traceId;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public void setProjectName(@Nullable String projectName) {
        this.projectName = projectName;
    }

    public void setEvalName(@Nullable String evalName) {
        this.evalName = evalName;
    }

    public void setCustomScorers(@Nullable List<BaseScorer> customScorers) {
        this.customScorers = customScorers;
    }

    public void setJudgmentScorers(@Nullable List<ScorerConfig> judgmentScorers) {
        this.judgmentScorers = judgmentScorers;
    }

    public void setModel(@Nonnull String model) {
        this.model = model;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    public void setExamples(@Nonnull List<Example> examples) {
        this.examples = examples;
    }

    public void setTraceSpanId(@Nullable String traceSpanId) {
        this.traceSpanId = traceSpanId;
    }

    public void setTraceId(@Nullable String traceId) {
        this.traceId = traceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExampleEvaluationRun other = (ExampleEvaluationRun) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(projectName, other.projectName)
                && Objects.equals(evalName, other.evalName)
                && Objects.equals(customScorers, other.customScorers)
                && Objects.equals(judgmentScorers, other.judgmentScorers)
                && Objects.equals(model, other.model)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(examples, other.examples)
                && Objects.equals(traceSpanId, other.traceSpanId)
                && Objects.equals(traceId, other.traceId)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                projectName,
                evalName,
                customScorers,
                judgmentScorers,
                model,
                createdAt,
                examples,
                traceSpanId,
                traceId,
                Objects.hashCode(additionalProperties));
    }
}
