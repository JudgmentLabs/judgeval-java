package com.judgmentlabs.judgeval.data;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScorerData {
    @JsonProperty("name")
    private String              name;
    @JsonProperty("score")
    private Double              score;
    @JsonProperty("success")
    private Boolean             success;
    @JsonProperty("reason")
    private String              reason;
    @JsonProperty("threshold")
    private Double              threshold;
    @JsonProperty("strict_mode")
    private Boolean             strictMode;
    @JsonProperty("evaluation_model")
    private String              evaluationModel;
    @JsonProperty("error")
    private String              error;
    @JsonProperty("additional_metadata")
    private Object              additionalMetadata;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String key, Object value) {
        additionalProperties.put(key, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Boolean getStrictMode() {
        return strictMode;
    }

    public void setStrictMode(Boolean strictMode) {
        this.strictMode = strictMode;
    }

    public String getEvaluationModel() {
        return evaluationModel;
    }

    public void setEvaluationModel(String evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final ScorerData scorerData;

        private Builder() {
            this.scorerData = new ScorerData();
        }

        public Builder name(String name) {
            scorerData.setName(name);
            return this;
        }

        public Builder score(Double score) {
            scorerData.setScore(score);
            return this;
        }

        public Builder success(Boolean success) {
            scorerData.setSuccess(success);
            return this;
        }

        public Builder reason(String reason) {
            scorerData.setReason(reason);
            return this;
        }

        public Builder threshold(Double threshold) {
            scorerData.setThreshold(threshold);
            return this;
        }

        public Builder strictMode(Boolean strictMode) {
            scorerData.setStrictMode(strictMode);
            return this;
        }

        public Builder evaluationModel(String evaluationModel) {
            scorerData.setEvaluationModel(evaluationModel);
            return this;
        }

        public Builder error(String error) {
            scorerData.setError(error);
            return this;
        }

        public Builder additionalMetadata(Map<String, Object> additionalMetadata) {
            scorerData.setAdditionalMetadata(additionalMetadata);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder metadata(String key, Object value) {
            if (scorerData.getAdditionalMetadata() == null) {
                scorerData.setAdditionalMetadata(new java.util.HashMap<>());
            }
            Map<String, Object> metadata = (Map<String, Object>) scorerData.getAdditionalMetadata();
            metadata.put(key, value);
            return this;
        }

        public ScorerData build() {
            return scorerData;
        }
    }
}
