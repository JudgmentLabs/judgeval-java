package com.judgmentlabs.judgeval.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class SavePromptScorerRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("prompt")
    private String prompt;
    @JsonProperty("threshold")
    private Double threshold;
    @JsonProperty("options")
    private Object options;
    @JsonProperty("is_trace")
    private Object isTrace;

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
    public Object getIsTrace() {
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
    public void setIsTrace(Object isTrace) {
        this.isTrace = isTrace;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SavePromptScorerRequest other = (SavePromptScorerRequest) obj;
        return Objects.equals(name, other.name) && Objects.equals(prompt, other.prompt) && Objects.equals(threshold, other.threshold) && Objects.equals(options, other.options) && Objects.equals(isTrace, other.isTrace) && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name) + Objects.hashCode(prompt) + Objects.hashCode(threshold) + Objects.hashCode(options) + Objects.hashCode(isTrace) + Objects.hashCode(additionalProperties);
    }
}