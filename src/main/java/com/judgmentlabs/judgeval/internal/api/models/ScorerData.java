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

public class ScorerData {
    @JsonProperty("name")
    @Nonnull
    private String name;

    @JsonProperty("threshold")
    @Nonnull
    private Double threshold;

    @JsonProperty("success")
    @Nonnull
    private Boolean success;

    @JsonProperty("score")
    @Nullable
    private Double score;

    @JsonProperty("reason")
    @Nullable
    private String reason;

    @JsonProperty("strict_mode")
    @Nullable
    private Boolean strictMode;

    @JsonProperty("evaluation_model")
    @Nullable
    private List<String> evaluationModel;

    @JsonProperty("error")
    @Nullable
    private String error;

    @JsonProperty("additional_metadata")
    @Nullable
    private Object additionalMetadata;

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
    public String getName() {
        return name;
    }

    @Nonnull
    public Double getThreshold() {
        return threshold;
    }

    @Nonnull
    public Boolean getSuccess() {
        return success;
    }

    @Nullable
    public Double getScore() {
        return score;
    }

    @Nullable
    public String getReason() {
        return reason;
    }

    @Nullable
    public Boolean getStrictMode() {
        return strictMode;
    }

    @Nullable
    public List<String> getEvaluationModel() {
        return evaluationModel;
    }

    @Nullable
    public String getError() {
        return error;
    }

    @Nullable
    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public void setThreshold(@Nonnull Double threshold) {
        this.threshold = threshold;
    }

    public void setSuccess(@Nonnull Boolean success) {
        this.success = success;
    }

    public void setScore(@Nullable Double score) {
        this.score = score;
    }

    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

    public void setStrictMode(@Nullable Boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setEvaluationModel(@Nullable List<String> evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    public void setError(@Nullable String error) {
        this.error = error;
    }

    public void setAdditionalMetadata(@Nullable Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScorerData other = (ScorerData) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(threshold, other.threshold)
                && Objects.equals(success, other.success)
                && Objects.equals(score, other.score)
                && Objects.equals(reason, other.reason)
                && Objects.equals(strictMode, other.strictMode)
                && Objects.equals(evaluationModel, other.evaluationModel)
                && Objects.equals(error, other.error)
                && Objects.equals(additionalMetadata, other.additionalMetadata)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                threshold,
                success,
                score,
                reason,
                strictMode,
                evaluationModel,
                error,
                additionalMetadata,
                Objects.hashCode(additionalProperties));
    }
}
