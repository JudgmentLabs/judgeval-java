package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateDatasetRequest {
    @JsonProperty("name")
    private String              name;
    @JsonProperty("dataset_kind")
    private String              datasetKind;
    @JsonProperty("examples")
    private List<Example>       examples;
    @JsonProperty("overwrite")
    private Boolean             overwrite;

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

    public String getDatasetKind() {
        return datasetKind;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatasetKind(String datasetKind) {
        this.datasetKind = datasetKind;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CreateDatasetRequest other = (CreateDatasetRequest) obj;
        return Objects.equals(name, other.name) && Objects.equals(datasetKind, other.datasetKind)
                && Objects.equals(examples, other.examples) && Objects.equals(overwrite, other.overwrite)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, datasetKind, examples, overwrite, Objects.hashCode(additionalProperties));
    }
}