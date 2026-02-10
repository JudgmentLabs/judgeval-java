package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TriggerRootSpanRulesRequest {
    @JsonProperty("traces")
    private List<TraceInfo>     traces;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public List<TraceInfo> getTraces() {
        return traces;
    }

    public void setTraces(List<TraceInfo> traces) {
        this.traces = traces;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TriggerRootSpanRulesRequest other = (TriggerRootSpanRulesRequest) obj;
        return Objects.equals(traces, other.traces) && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traces, Objects.hashCode(additionalProperties));
    }
}