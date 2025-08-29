package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScorerConfig {
    @JsonProperty("score_type")
    private String scoreType;

    @JsonProperty("name")
    private Object name;

    @JsonProperty("threshold")
    private Double threshold;

    @JsonProperty("strict_mode")
    private Boolean strictMode;

    @JsonProperty("required_params")
    private List<String> requiredParams;

    @JsonProperty("kwargs")
    private Object kwargs;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getScoreType() {
        return scoreType;
    }

    public Object getName() {
        return name;
    }

    public Double getThreshold() {
        return threshold;
    }

    public Boolean getStrictMode() {
        return strictMode;
    }

    public List<String> getRequiredParams() {
        return requiredParams;
    }

    public Object getKwargs() {
        return kwargs;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setStrictMode(Boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setRequiredParams(List<String> requiredParams) {
        this.requiredParams = requiredParams;
    }

    public void setKwargs(Object kwargs) {
        this.kwargs = kwargs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScorerConfig other = (ScorerConfig) obj;
        return Objects.equals(scoreType, other.scoreType)
                && Objects.equals(name, other.name)
                && Objects.equals(threshold, other.threshold)
                && Objects.equals(strictMode, other.strictMode)
                && Objects.equals(requiredParams, other.requiredParams)
                && Objects.equals(kwargs, other.kwargs)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                scoreType,
                name,
                threshold,
                strictMode,
                requiredParams,
                kwargs,
                Objects.hashCode(additionalProperties));
    }
}
