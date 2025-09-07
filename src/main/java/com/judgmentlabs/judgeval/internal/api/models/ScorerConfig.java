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

public class ScorerConfig {
    @JsonProperty("score_type")
    @Nonnull
    private String scoreType;

    @JsonProperty("name")
    @Nullable
    private String name;

    @JsonProperty("threshold")
    @Nullable
    private Double threshold;

    @JsonProperty("strict_mode")
    @Nullable
    private Boolean strictMode;

    @JsonProperty("required_params")
    @Nullable
    private List<String> requiredParams;

    @JsonProperty("kwargs")
    @Nullable
    private Object kwargs;

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
    public String getScoreType() {
        return scoreType;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public Double getThreshold() {
        return threshold;
    }

    @Nullable
    public Boolean getStrictMode() {
        return strictMode;
    }

    @Nullable
    public List<String> getRequiredParams() {
        return requiredParams;
    }

    @Nullable
    public Object getKwargs() {
        return kwargs;
    }

    public void setScoreType(@Nonnull String scoreType) {
        this.scoreType = scoreType;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setThreshold(@Nullable Double threshold) {
        this.threshold = threshold;
    }

    public void setStrictMode(@Nullable Boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setRequiredParams(@Nullable List<String> requiredParams) {
        this.requiredParams = requiredParams;
    }

    public void setKwargs(@Nullable Object kwargs) {
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
