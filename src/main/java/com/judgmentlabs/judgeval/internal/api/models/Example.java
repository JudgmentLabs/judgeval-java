package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Example {
    @JsonProperty("example_id")
    @Nonnull
    private String exampleId;

    @JsonProperty("created_at")
    @Nonnull
    private String createdAt;

    @JsonProperty("name")
    @Nullable
    private String name;

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

    @Nonnull
    public String getExampleId() {
        return exampleId;
    }

    @Nonnull
    public String getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setExampleId(@Nonnull String exampleId) {
        this.exampleId = exampleId;
    }

    public void setCreatedAt(@Nonnull String createdAt) {
        this.createdAt = createdAt;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Example other = (Example) obj;
        return Objects.equals(exampleId, other.exampleId)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(name, other.name)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exampleId, createdAt, name, Objects.hashCode(additionalProperties));
    }
}
