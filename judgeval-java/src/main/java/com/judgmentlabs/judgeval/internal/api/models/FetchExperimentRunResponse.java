package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchExperimentRunResponse {
    @JsonProperty("results")
    private List<Object>        results;
    @JsonProperty("ui_results_url")
    private String              uiResultsUrl;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public List<Object> getResults() {
        return results;
    }

    public String getUiResultsUrl() {
        return uiResultsUrl;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    public void setUiResultsUrl(String uiResultsUrl) {
        this.uiResultsUrl = uiResultsUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        FetchExperimentRunResponse other = (FetchExperimentRunResponse) obj;
        return Objects.equals(results, other.results) && Objects.equals(uiResultsUrl, other.uiResultsUrl)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results, uiResultsUrl, Objects.hashCode(additionalProperties));
    }
}