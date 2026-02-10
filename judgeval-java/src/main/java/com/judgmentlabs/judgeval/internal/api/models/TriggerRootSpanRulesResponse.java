package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TriggerRootSpanRulesResponse {
    @JsonProperty("success")
    private Boolean             success;
    @JsonProperty("queued_traces")
    private Double              queuedTraces;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public Boolean getSuccess() {
        return success;
    }

    public Double getQueuedTraces() {
        return queuedTraces;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setQueuedTraces(Double queuedTraces) {
        this.queuedTraces = queuedTraces;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TriggerRootSpanRulesResponse other = (TriggerRootSpanRulesResponse) obj;
        return Objects.equals(success, other.success) && Objects.equals(queuedTraces, other.queuedTraces)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, queuedTraces, Objects.hashCode(additionalProperties));
    }
}