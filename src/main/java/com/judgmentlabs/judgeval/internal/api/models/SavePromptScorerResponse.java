package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SavePromptScorerResponse {
    @JsonProperty("message")
    @Nonnull
    private String message;

    @JsonProperty("name")
    @Nonnull
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
    public String getMessage() {
        return message;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setMessage(@Nonnull String message) {
        this.message = message;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SavePromptScorerResponse other = (SavePromptScorerResponse) obj;
        return Objects.equals(message, other.message)
                && Objects.equals(name, other.name)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, name, Objects.hashCode(additionalProperties));
    }
}
