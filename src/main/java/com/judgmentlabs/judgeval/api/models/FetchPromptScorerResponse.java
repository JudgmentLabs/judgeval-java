package com.judgmentlabs.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchPromptScorerResponse {
    @JsonProperty("scorer")
    private PromptScorer scorer;

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
        return Objects.equals(scorer, other.scorer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scorer);
    }
}
