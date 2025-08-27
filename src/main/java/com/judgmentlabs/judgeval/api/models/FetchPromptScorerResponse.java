package com.judgmentlabs.judgeval.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchPromptScorerResponse {
    @JsonProperty("scorer")
    private PromptScorer scorer;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public PromptScorer getScorer() {
        return scorer;
    }

    public void setScorer(PromptScorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FetchPromptScorerResponse other = (FetchPromptScorerResponse) obj;
        return Objects.equals(scorer, other.scorer)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scorer) + Objects.hashCode(additionalProperties);
    }
}
