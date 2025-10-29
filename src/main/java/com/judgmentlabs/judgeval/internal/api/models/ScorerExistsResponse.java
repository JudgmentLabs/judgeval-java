package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScorerExistsResponse {
    @JsonProperty("exists")
    @NotNull
    private Boolean exists;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(@NotNull Boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ScorerExistsResponse other = (ScorerExistsResponse) obj;
        return Objects.equals(exists, other.exists)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exists, Objects.hashCode(additionalProperties));
    }
}
