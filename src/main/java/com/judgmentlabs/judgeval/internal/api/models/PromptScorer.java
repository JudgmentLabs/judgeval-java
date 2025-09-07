package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PromptScorer {
    @JsonProperty("name")
    @Nonnull
    private String name;

    @JsonProperty("prompt")
    @Nonnull
    private String prompt;

    @JsonProperty("threshold")
    @Nonnull
    private Double threshold;

    @JsonProperty("options")
    @Nullable
    private Object options;

    @JsonProperty("created_at")
    @Nullable
    private String createdAt;

    @JsonProperty("updated_at")
    @Nullable
    private String updatedAt;

    @JsonProperty("is_trace")
    @Nullable
    private Boolean isTrace;

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
    public String getPrompt() {
        return prompt;
    }

    @Nonnull
    public Double getThreshold() {
        return threshold;
    }

    @Nullable
    public Object getOptions() {
        return options;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Nullable
    public Boolean getIsTrace() {
        return isTrace;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public void setPrompt(@Nonnull String prompt) {
        this.prompt = prompt;
    }

    public void setThreshold(@Nonnull Double threshold) {
        this.threshold = threshold;
    }

    public void setOptions(@Nullable Object options) {
        this.options = options;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(@Nullable String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsTrace(@Nullable Boolean isTrace) {
        this.isTrace = isTrace;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PromptScorer other = (PromptScorer) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(prompt, other.prompt)
                && Objects.equals(threshold, other.threshold)
                && Objects.equals(options, other.options)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(updatedAt, other.updatedAt)
                && Objects.equals(isTrace, other.isTrace)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                prompt,
                threshold,
                options,
                createdAt,
                updatedAt,
                isTrace,
                Objects.hashCode(additionalProperties));
    }
}
