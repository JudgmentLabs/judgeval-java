package com.judgmentlabs.judgeval.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Example {
    @JsonProperty("example_id")
    private String exampleId;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("name")
    private Object name;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getExampleId() {
        return exampleId;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public Object getName() {
        return name;
    }

    public void setExampleId(String exampleId) {
        this.exampleId = exampleId;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public void setName(Object name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Example other = (Example) obj;
        return Objects.equals(exampleId, other.exampleId) && Objects.equals(createdAt, other.createdAt) && Objects.equals(name, other.name) && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exampleId) + Objects.hashCode(createdAt) + Objects.hashCode(name) + Objects.hashCode(additionalProperties);
    }
}