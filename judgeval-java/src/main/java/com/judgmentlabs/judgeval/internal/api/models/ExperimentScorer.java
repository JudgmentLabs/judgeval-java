package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperimentScorer {
    @JsonProperty("scorer_data_id")
    private String              scorerDataId;
    @JsonProperty("name")
    private String              name;
    @JsonProperty("score")
    private Double              score;
    @JsonProperty("success")
    private Double              success;
    @JsonProperty("reason")
    private String              reason;
    @JsonProperty("evaluation_model")
    private String              evaluationModel;
    @JsonProperty("threshold")
    private Double              threshold;
    @JsonProperty("created_at")
    private String              createdAt;
    @JsonProperty("error")
    private String              error;
    @JsonProperty("additional_metadata")
    private Object              additionalMetadata;
    @JsonProperty("minimum_score_range")
    private Double              minimumScoreRange;
    @JsonProperty("maximum_score_range")
    private Double              maximumScoreRange;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getScorerDataId() {
        return scorerDataId;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    public Double getSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }

    public String getEvaluationModel() {
        return evaluationModel;
    }

    public Double getThreshold() {
        return threshold;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getError() {
        return error;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public Double getMinimumScoreRange() {
        return minimumScoreRange;
    }

    public Double getMaximumScoreRange() {
        return maximumScoreRange;
    }

    public void setScorerDataId(String scorerDataId) {
        this.scorerDataId = scorerDataId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setSuccess(Double success) {
        this.success = success;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setEvaluationModel(String evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setMinimumScoreRange(Double minimumScoreRange) {
        this.minimumScoreRange = minimumScoreRange;
    }

    public void setMaximumScoreRange(Double maximumScoreRange) {
        this.maximumScoreRange = maximumScoreRange;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ExperimentScorer other = (ExperimentScorer) obj;
        return Objects.equals(scorerDataId, other.scorerDataId) && Objects.equals(name, other.name)
                && Objects.equals(score, other.score) && Objects.equals(success, other.success)
                && Objects.equals(reason, other.reason) && Objects.equals(evaluationModel, other.evaluationModel)
                && Objects.equals(threshold, other.threshold) && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(error, other.error) && Objects.equals(additionalMetadata, other.additionalMetadata)
                && Objects.equals(minimumScoreRange, other.minimumScoreRange)
                && Objects.equals(maximumScoreRange, other.maximumScoreRange)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scorerDataId, name, score, success, reason, evaluationModel, threshold, createdAt, error,
                additionalMetadata, minimumScoreRange, maximumScoreRange, Objects.hashCode(additionalProperties));
    }
}