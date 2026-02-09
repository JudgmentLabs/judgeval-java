package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DatasetInfo {
    @JsonProperty("dataset_id")
    private String              datasetId;
    @JsonProperty("name")
    private String              name;
    @JsonProperty("created_at")
    private String              createdAt;
    @JsonProperty("kind")
    private String              kind;
    @JsonProperty("entries")
    private Double              entries;
    @JsonProperty("creator")
    private String              creator;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getDatasetId() {
        return datasetId;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getKind() {
        return kind;
    }

    public Double getEntries() {
        return entries;
    }

    public String getCreator() {
        return creator;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setEntries(Double entries) {
        this.entries = entries;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DatasetInfo other = (DatasetInfo) obj;
        return Objects.equals(datasetId, other.datasetId) && Objects.equals(name, other.name)
                && Objects.equals(createdAt, other.createdAt) && Objects.equals(kind, other.kind)
                && Objects.equals(entries, other.entries) && Objects.equals(creator, other.creator)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasetId, name, createdAt, kind, entries, creator, Objects.hashCode(additionalProperties));
    }
}