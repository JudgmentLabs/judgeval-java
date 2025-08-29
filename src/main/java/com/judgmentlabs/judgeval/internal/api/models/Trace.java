package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Trace {
    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("duration")
    private Double duration;

    @JsonProperty("trace_spans")
    private List<TraceSpan> traceSpans;

    @JsonProperty("offline_mode")
    private Boolean offlineMode;

    @JsonProperty("rules")
    private Object rules;

    @JsonProperty("has_notification")
    private Boolean hasNotification;

    @JsonProperty("customer_id")
    private Object customerId;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("metadata")
    private Object metadata;

    @JsonProperty("update_id")
    private Integer updateId;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getTraceId() {
        return traceId;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Double getDuration() {
        return duration;
    }

    public List<TraceSpan> getTraceSpans() {
        return traceSpans;
    }

    public Boolean getOfflineMode() {
        return offlineMode;
    }

    public Object getRules() {
        return rules;
    }

    public Boolean getHasNotification() {
        return hasNotification;
    }

    public Object getCustomerId() {
        return customerId;
    }

    public List<String> getTags() {
        return tags;
    }

    public Object getMetadata() {
        return metadata;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setTraceSpans(List<TraceSpan> traceSpans) {
        this.traceSpans = traceSpans;
    }

    public void setOfflineMode(Boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public void setRules(Object rules) {
        this.rules = rules;
    }

    public void setHasNotification(Boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trace other = (Trace) obj;
        return Objects.equals(traceId, other.traceId)
                && Objects.equals(name, other.name)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(duration, other.duration)
                && Objects.equals(traceSpans, other.traceSpans)
                && Objects.equals(offlineMode, other.offlineMode)
                && Objects.equals(rules, other.rules)
                && Objects.equals(hasNotification, other.hasNotification)
                && Objects.equals(customerId, other.customerId)
                && Objects.equals(tags, other.tags)
                && Objects.equals(metadata, other.metadata)
                && Objects.equals(updateId, other.updateId)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                traceId,
                name,
                createdAt,
                duration,
                traceSpans,
                offlineMode,
                rules,
                hasNotification,
                customerId,
                tags,
                metadata,
                updateId,
                Objects.hashCode(additionalProperties));
    }
}
