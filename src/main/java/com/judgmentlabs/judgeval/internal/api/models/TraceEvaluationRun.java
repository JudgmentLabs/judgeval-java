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

public class TraceEvaluationRun {
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

    @JsonProperty("trace_and_span_ids")
    @Nonnull
    private List<List<Object>> traceAndSpanIds;

    @JsonProperty("is_offline")
    @Nullable
    private Boolean isOffline;

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
    public List<List<Object>> getTraceAndSpanIds() {
        return traceAndSpanIds;
    }

    @Nullable
    public Boolean getIsOffline() {
        return isOffline;
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

    public void setTraceAndSpanIds(@Nonnull List<List<Object>> traceAndSpanIds) {
        this.traceAndSpanIds = traceAndSpanIds;
    }

    public void setIsOffline(@Nullable Boolean isOffline) {
        this.isOffline = isOffline;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TraceEvaluationRun other = (TraceEvaluationRun) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(projectName, other.projectName)
                && Objects.equals(evalName, other.evalName)
                && Objects.equals(customScorers, other.customScorers)
                && Objects.equals(judgmentScorers, other.judgmentScorers)
                && Objects.equals(model, other.model)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(traceAndSpanIds, other.traceAndSpanIds)
                && Objects.equals(isOffline, other.isOffline)
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
                traceAndSpanIds,
                isOffline,
                Objects.hashCode(additionalProperties));
    }
}
