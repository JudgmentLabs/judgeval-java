package com.judgmentlabs.judgeval.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class EvalResults {
    @JsonProperty("results")
    private List<ScoringResult> results;
    @JsonProperty("run")
    private Object run;

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

    public void setResults(List<ScoringResult> results) {
        this.results = results;
    }
    public void setRun(Object run) {
        this.run = run;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EvalResults other = (EvalResults) obj;
        return Objects.equals(results, other.results) && Objects.equals(run, other.run) && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(results) + Objects.hashCode(run) + Objects.hashCode(additionalProperties);
    }
}