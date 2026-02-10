package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PullDatasetResponse {
    @JsonProperty("name")
    private String              name;
    @JsonProperty("project_id")
    private String              projectId;
    @JsonProperty("dataset_kind")
    private String              datasetKind;
    @JsonProperty("examples")
    private List<Example>       examples;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getName() {
        return name;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getDatasetKind() {
        return datasetKind;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setDatasetKind(String datasetKind) {
        this.datasetKind = datasetKind;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PullDatasetResponse other = (PullDatasetResponse) obj;
        return Objects.equals(name, other.name) && Objects.equals(projectId, other.projectId)
                && Objects.equals(datasetKind, other.datasetKind) && Objects.equals(examples, other.examples)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, projectId, datasetKind, examples, Objects.hashCode(additionalProperties));
    }
}