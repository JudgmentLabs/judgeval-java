package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PromptScorer {
    @JsonProperty("id")
    @NotNull
    private String id;
    @JsonProperty("user_id")
    @NotNull
    private String userId;
    @JsonProperty("organization_id")
    @NotNull
    private String organizationId;
    @JsonProperty("name")
    @NotNull
    private String name;
    @JsonProperty("prompt")
    @NotNull
    private String prompt;
    @JsonProperty("threshold")
    @NotNull
    private Double threshold;
    @JsonProperty("model")
    private String model;
    @JsonProperty("options")
    private Object options;
    @JsonProperty("description")
    private String description;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("is_trace")
    private Boolean isTrace;
    @JsonProperty("is_bucket_rubric")
    private Boolean isBucketRubric;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrganizationId() {
        return organizationId;
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

    public String getModel() {
        return model;
    }

    public Object getOptions() {
        return options;
    }

    public String getDescription() {
        return description;
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

    public Boolean getIsBucketRubric() {
        return isBucketRubric;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public void setOrganizationId(@NotNull String organizationId) {
        this.organizationId = organizationId;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setPrompt(@NotNull String prompt) {
        this.prompt = prompt;
    }

    public void setThreshold(@NotNull Double threshold) {
        this.threshold = threshold;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setOptions(Object options) {
        this.options = options;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setIsBucketRubric(Boolean isBucketRubric) {
        this.isBucketRubric = isBucketRubric;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PromptScorer other = (PromptScorer) obj;
        return Objects.equals(id, other.id) && Objects.equals(userId, other.userId)
                && Objects.equals(organizationId, other.organizationId)
                && Objects.equals(name, other.name)
                && Objects.equals(prompt, other.prompt)
                && Objects.equals(threshold, other.threshold)
                && Objects.equals(model, other.model) && Objects.equals(options, other.options)
                && Objects.equals(description, other.description)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(updatedAt, other.updatedAt)
                && Objects.equals(isTrace, other.isTrace)
                && Objects.equals(isBucketRubric, other.isBucketRubric)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, organizationId, name, prompt, threshold, model, options,
                description, createdAt, updatedAt, isTrace, isBucketRubric,
                Objects.hashCode(additionalProperties));
    }
}
