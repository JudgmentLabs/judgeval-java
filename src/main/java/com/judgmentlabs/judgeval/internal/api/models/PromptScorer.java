package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PromptScorer {
    @JsonProperty("name")
    private String name;

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("threshold")
    private Double threshold;

    @JsonProperty("options")
    private Object options;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("is_trace")
    private Boolean isTrace;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getName() {
        return name;
    }

    public String getPrompt() {
        return prompt;
    }

    public Double getThreshold() {
        return threshold;
    }

    public Object getOptions() {
        return options;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getIsTrace() {
        return isTrace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setOptions(Object options) {
        this.options = options;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsTrace(Boolean isTrace) {
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
