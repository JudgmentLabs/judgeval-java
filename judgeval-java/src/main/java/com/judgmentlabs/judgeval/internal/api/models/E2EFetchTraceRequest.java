package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class E2EFetchTraceRequest {
    @JsonProperty("project_name")
    private String              projectName;
    @JsonProperty("trace_id")
    private String              traceId;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        E2EFetchTraceRequest other = (E2EFetchTraceRequest) obj;
        return Objects.equals(projectName, other.projectName) && Objects.equals(traceId, other.traceId)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, traceId, Objects.hashCode(additionalProperties));
    }
}