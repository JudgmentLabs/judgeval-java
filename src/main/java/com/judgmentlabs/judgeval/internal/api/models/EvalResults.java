package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalResults {
    @JsonProperty("results")
    @Nonnull
    private List<ScoringResult> results;

    @JsonProperty("run")
    @Nonnull
    private TraceEvaluationRun run;

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
    public List<ScoringResult> getResults() {
        return results;
    }

    @Nonnull
    public TraceEvaluationRun getRun() {
        return run;
    }

    public void setResults(@Nonnull List<ScoringResult> results) {
        this.results = results;
    }

    public void setRun(@Nonnull TraceEvaluationRun run) {
        this.run = run;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EvalResults other = (EvalResults) obj;
        return Objects.equals(results, other.results)
                && Objects.equals(run, other.run)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results, run, Objects.hashCode(additionalProperties));
    }
}
