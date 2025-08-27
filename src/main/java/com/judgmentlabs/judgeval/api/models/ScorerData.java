package com.judgmentlabs.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScorerData {
    @JsonProperty("name")
    private String name;

    @JsonProperty("threshold")
    private Double threshold;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("score")
    private Object score;

    @JsonProperty("reason")
    private Object reason;

    @JsonProperty("strict_mode")
    private Object strictMode;

    @JsonProperty("evaluation_model")
    private Object evaluationModel;

    @JsonProperty("error")
    private Object error;

    @JsonProperty("additional_metadata")
    private Object additionalMetadata;

    public String getName() {
        return name;
    }

    public Double getThreshold() {
        return threshold;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Object getScore() {
        return score;
    }

    public Object getReason() {
        return reason;
    }

    public Object getStrictMode() {
        return strictMode;
    }

    public Object getEvaluationModel() {
        return evaluationModel;
    }

    public Object getError() {
        return error;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setScore(Object score) {
        this.score = score;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public void setStrictMode(Object strictMode) {
        this.strictMode = strictMode;
    }

    public void setEvaluationModel(Object evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
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
                && Objects.equals(additionalMetadata, other.additionalMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name)
                + Objects.hashCode(threshold)
                + Objects.hashCode(success)
                + Objects.hashCode(score)
                + Objects.hashCode(reason)
                + Objects.hashCode(strictMode)
                + Objects.hashCode(evaluationModel)
                + Objects.hashCode(error)
                + Objects.hashCode(additionalMetadata);
    }
}
