package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalResults {
    @JsonProperty("results")
    @NotNull
    private List<ScoringResult> results;
    @JsonProperty("run")
    @NotNull
    private Object              run;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public List<ScoringResult> getResults() {
        return results;
    }

    public Object getRun() {
        return run;
    }

    public void setResults(@NotNull List<ScoringResult> results) {
        this.results = results;
    }

    public void setRun(@NotNull Object run) {
        this.run = run;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EvalResults other = (EvalResults) obj;
        return Objects.equals(results, other.results) && Objects.equals(run, other.run)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results, run, Objects.hashCode(additionalProperties));
    }
}
