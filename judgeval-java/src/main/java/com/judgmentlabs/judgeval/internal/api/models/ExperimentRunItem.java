package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperimentRunItem {
    @JsonProperty("organization_id")
    private String                 organizationId;
    @JsonProperty("experiment_run_id")
    private String                 experimentRunId;
    @JsonProperty("example_id")
    private String                 exampleId;
    @JsonProperty("data")
    private Object                 data;
    @JsonProperty("name")
    private String                 name;
    @JsonProperty("created_at")
    private String                 createdAt;
    @JsonProperty("scorers")
    private List<ExperimentScorer> scorers;

    private Map<String, Object>    additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getExperimentRunId() {
        return experimentRunId;
    }

    public String getExampleId() {
        return exampleId;
    }

    public Object getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<ExperimentScorer> getScorers() {
        return scorers;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setExperimentRunId(String experimentRunId) {
        this.experimentRunId = experimentRunId;
    }

    public void setExampleId(String exampleId) {
        this.exampleId = exampleId;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setScorers(List<ExperimentScorer> scorers) {
        this.scorers = scorers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ExperimentRunItem other = (ExperimentRunItem) obj;
        return Objects.equals(organizationId, other.organizationId)
                && Objects.equals(experimentRunId, other.experimentRunId) && Objects.equals(exampleId, other.exampleId)
                && Objects.equals(data, other.data) && Objects.equals(name, other.name)
                && Objects.equals(createdAt, other.createdAt) && Objects.equals(scorers, other.scorers)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, experimentRunId, exampleId, data, name, createdAt, scorers,
                Objects.hashCode(additionalProperties));
    }
}