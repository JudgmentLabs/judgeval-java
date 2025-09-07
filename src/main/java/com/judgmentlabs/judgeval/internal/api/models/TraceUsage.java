package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceUsage {
    @JsonProperty("prompt_tokens")
    @Nullable
    private Integer promptTokens;

    @JsonProperty("completion_tokens")
    @Nullable
    private Integer completionTokens;

    @JsonProperty("cache_creation_input_tokens")
    @Nullable
    private Integer cacheCreationInputTokens;

    @JsonProperty("cache_read_input_tokens")
    @Nullable
    private Integer cacheReadInputTokens;

    @JsonProperty("total_tokens")
    @Nullable
    private Integer totalTokens;

    @JsonProperty("prompt_tokens_cost_usd")
    @Nullable
    private Double promptTokensCostUsd;

    @JsonProperty("completion_tokens_cost_usd")
    @Nullable
    private Double completionTokensCostUsd;

    @JsonProperty("total_cost_usd")
    @Nullable
    private Double totalCostUsd;

    @JsonProperty("model_name")
    @Nullable
    private String modelName;

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

    @Nullable
    public Integer getPromptTokens() {
        return promptTokens;
    }

    @Nullable
    public Integer getCompletionTokens() {
        return completionTokens;
    }

    @Nullable
    public Integer getCacheCreationInputTokens() {
        return cacheCreationInputTokens;
    }

    @Nullable
    public Integer getCacheReadInputTokens() {
        return cacheReadInputTokens;
    }

    @Nullable
    public Integer getTotalTokens() {
        return totalTokens;
    }

    @Nullable
    public Double getPromptTokensCostUsd() {
        return promptTokensCostUsd;
    }

    @Nullable
    public Double getCompletionTokensCostUsd() {
        return completionTokensCostUsd;
    }

    @Nullable
    public Double getTotalCostUsd() {
        return totalCostUsd;
    }

    @Nullable
    public String getModelName() {
        return modelName;
    }

    public void setPromptTokens(@Nullable Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public void setCompletionTokens(@Nullable Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public void setCacheCreationInputTokens(@Nullable Integer cacheCreationInputTokens) {
        this.cacheCreationInputTokens = cacheCreationInputTokens;
    }

    public void setCacheReadInputTokens(@Nullable Integer cacheReadInputTokens) {
        this.cacheReadInputTokens = cacheReadInputTokens;
    }

    public void setTotalTokens(@Nullable Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public void setPromptTokensCostUsd(@Nullable Double promptTokensCostUsd) {
        this.promptTokensCostUsd = promptTokensCostUsd;
    }

    public void setCompletionTokensCostUsd(@Nullable Double completionTokensCostUsd) {
        this.completionTokensCostUsd = completionTokensCostUsd;
    }

    public void setTotalCostUsd(@Nullable Double totalCostUsd) {
        this.totalCostUsd = totalCostUsd;
    }

    public void setModelName(@Nullable String modelName) {
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
