package com.judgmentlabs.judgeval.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceUsage {
    @JsonProperty("prompt_tokens")
    private Object promptTokens;

    @JsonProperty("completion_tokens")
    private Object completionTokens;

    @JsonProperty("cache_creation_input_tokens")
    private Object cacheCreationInputTokens;

    @JsonProperty("cache_read_input_tokens")
    private Object cacheReadInputTokens;

    @JsonProperty("total_tokens")
    private Object totalTokens;

    @JsonProperty("prompt_tokens_cost_usd")
    private Object promptTokensCostUsd;

    @JsonProperty("completion_tokens_cost_usd")
    private Object completionTokensCostUsd;

    @JsonProperty("total_cost_usd")
    private Object totalCostUsd;

    @JsonProperty("model_name")
    private Object modelName;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public Object getPromptTokens() {
        return promptTokens;
    }

    public Object getCompletionTokens() {
        return completionTokens;
    }

    public Object getCacheCreationInputTokens() {
        return cacheCreationInputTokens;
    }

    public Object getCacheReadInputTokens() {
        return cacheReadInputTokens;
    }

    public Object getTotalTokens() {
        return totalTokens;
    }

    public Object getPromptTokensCostUsd() {
        return promptTokensCostUsd;
    }

    public Object getCompletionTokensCostUsd() {
        return completionTokensCostUsd;
    }

    public Object getTotalCostUsd() {
        return totalCostUsd;
    }

    public Object getModelName() {
        return modelName;
    }

    public void setPromptTokens(Object promptTokens) {
        this.promptTokens = promptTokens;
    }

    public void setCompletionTokens(Object completionTokens) {
        this.completionTokens = completionTokens;
    }

    public void setCacheCreationInputTokens(Object cacheCreationInputTokens) {
        this.cacheCreationInputTokens = cacheCreationInputTokens;
    }

    public void setCacheReadInputTokens(Object cacheReadInputTokens) {
        this.cacheReadInputTokens = cacheReadInputTokens;
    }

    public void setTotalTokens(Object totalTokens) {
        this.totalTokens = totalTokens;
    }

    public void setPromptTokensCostUsd(Object promptTokensCostUsd) {
        this.promptTokensCostUsd = promptTokensCostUsd;
    }

    public void setCompletionTokensCostUsd(Object completionTokensCostUsd) {
        this.completionTokensCostUsd = completionTokensCostUsd;
    }

    public void setTotalCostUsd(Object totalCostUsd) {
        this.totalCostUsd = totalCostUsd;
    }

    public void setModelName(Object modelName) {
        this.modelName = modelName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TraceUsage other = (TraceUsage) obj;
        return Objects.equals(promptTokens, other.promptTokens)
                && Objects.equals(completionTokens, other.completionTokens)
                && Objects.equals(cacheCreationInputTokens, other.cacheCreationInputTokens)
                && Objects.equals(cacheReadInputTokens, other.cacheReadInputTokens)
                && Objects.equals(totalTokens, other.totalTokens)
                && Objects.equals(promptTokensCostUsd, other.promptTokensCostUsd)
                && Objects.equals(completionTokensCostUsd, other.completionTokensCostUsd)
                && Objects.equals(totalCostUsd, other.totalCostUsd)
                && Objects.equals(modelName, other.modelName)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(promptTokens)
                + Objects.hashCode(completionTokens)
                + Objects.hashCode(cacheCreationInputTokens)
                + Objects.hashCode(cacheReadInputTokens)
                + Objects.hashCode(totalTokens)
                + Objects.hashCode(promptTokensCostUsd)
                + Objects.hashCode(completionTokensCostUsd)
                + Objects.hashCode(totalCostUsd)
                + Objects.hashCode(modelName)
                + Objects.hashCode(additionalProperties);
    }
}
