package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceUsage {
    @JsonProperty("prompt_tokens")
    private Integer promptTokens;

    @JsonProperty("completion_tokens")
    private Integer completionTokens;

    @JsonProperty("cache_creation_input_tokens")
    private Integer cacheCreationInputTokens;

    @JsonProperty("cache_read_input_tokens")
    private Integer cacheReadInputTokens;

    @JsonProperty("total_tokens")
    private Integer totalTokens;

    @JsonProperty("prompt_tokens_cost_usd")
    private Double promptTokensCostUsd;

    @JsonProperty("completion_tokens_cost_usd")
    private Double completionTokensCostUsd;

    @JsonProperty("total_cost_usd")
    private Double totalCostUsd;

    @JsonProperty("model_name")
    private String modelName;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public Integer getCacheCreationInputTokens() {
        return cacheCreationInputTokens;
    }

    public Integer getCacheReadInputTokens() {
        return cacheReadInputTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public Double getPromptTokensCostUsd() {
        return promptTokensCostUsd;
    }

    public Double getCompletionTokensCostUsd() {
        return completionTokensCostUsd;
    }

    public Double getTotalCostUsd() {
        return totalCostUsd;
    }

    public String getModelName() {
        return modelName;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public void setCacheCreationInputTokens(Integer cacheCreationInputTokens) {
        this.cacheCreationInputTokens = cacheCreationInputTokens;
    }

    public void setCacheReadInputTokens(Integer cacheReadInputTokens) {
        this.cacheReadInputTokens = cacheReadInputTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public void setPromptTokensCostUsd(Double promptTokensCostUsd) {
        this.promptTokensCostUsd = promptTokensCostUsd;
    }

    public void setCompletionTokensCostUsd(Double completionTokensCostUsd) {
        this.completionTokensCostUsd = completionTokensCostUsd;
    }

    public void setTotalCostUsd(Double totalCostUsd) {
        this.totalCostUsd = totalCostUsd;
    }

    public void setModelName(String modelName) {
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
        return Objects.hash(
                promptTokens,
                completionTokens,
                cacheCreationInputTokens,
                cacheReadInputTokens,
                totalTokens,
                promptTokensCostUsd,
                completionTokensCostUsd,
                totalCostUsd,
                modelName,
                Objects.hashCode(additionalProperties));
    }
}
