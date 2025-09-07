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

public class ScoringResult {
    @JsonProperty("success")
    @Nonnull
    private Boolean success;

    @JsonProperty("scorers_data")
    @Nonnull
    private List<ScorerData> scorersData;

    @JsonProperty("name")
    @Nullable
    private String name;

    @JsonProperty("data_object")
    @Nullable
    private TraceSpan dataObject;

    @JsonProperty("trace_id")
    @Nullable
    private String traceId;

    @JsonProperty("run_duration")
    @Nullable
    private Double runDuration;

    @JsonProperty("evaluation_cost")
    @Nullable
    private Double evaluationCost;

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

    @Nonnull
    public Boolean getSuccess() {
        return success;
    }

    @Nonnull
    public List<ScorerData> getScorersData() {
        return scorersData;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public TraceSpan getDataObject() {
        return dataObject;
    }

    @Nullable
    public String getTraceId() {
        return traceId;
    }

    @Nullable
    public Double getRunDuration() {
        return runDuration;
    }

    @Nullable
    public Double getEvaluationCost() {
        return evaluationCost;
    }

    public void setSuccess(@Nonnull Boolean success) {
        this.success = success;
    }

    public void setScorersData(@Nonnull List<ScorerData> scorersData) {
        this.scorersData = scorersData;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setDataObject(@Nullable TraceSpan dataObject) {
        this.dataObject = dataObject;
    }

    public void setTraceId(@Nullable String traceId) {
        this.traceId = traceId;
    }

    public void setRunDuration(@Nullable Double runDuration) {
        this.runDuration = runDuration;
    }

    public void setEvaluationCost(@Nullable Double evaluationCost) {
        this.evaluationCost = evaluationCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScoringResult other = (ScoringResult) obj;
        return Objects.equals(success, other.success)
                && Objects.equals(scorersData, other.scorersData)
                && Objects.equals(name, other.name)
                && Objects.equals(dataObject, other.dataObject)
                && Objects.equals(traceId, other.traceId)
                && Objects.equals(runDuration, other.runDuration)
                && Objects.equals(evaluationCost, other.evaluationCost)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                success,
                scorersData,
                name,
                dataObject,
                traceId,
                runDuration,
                evaluationCost,
                Objects.hashCode(additionalProperties));
    }
}
