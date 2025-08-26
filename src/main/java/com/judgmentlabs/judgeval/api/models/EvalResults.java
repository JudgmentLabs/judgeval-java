package com.judgmentlabs.judgeval.api.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalResults {
    @JsonProperty("results")
    private List<ScoringResult> results;

    @JsonProperty("run")
    private Object run;

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
        return Objects.equals(results, other.results) && Objects.equals(run, other.run);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(results) + Objects.hashCode(run);
    }
}
