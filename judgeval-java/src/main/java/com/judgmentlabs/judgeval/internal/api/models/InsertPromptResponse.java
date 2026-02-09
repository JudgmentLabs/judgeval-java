package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertPromptResponse {
    @JsonProperty("commit_id")
    private String              commitId;
    @JsonProperty("parent_commit_id")
    private String              parentCommitId;
    @JsonProperty("created_at")
    private String              createdAt;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getCommitId() {
        return commitId;
    }

    public String getParentCommitId() {
        return parentCommitId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public void setParentCommitId(String parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        InsertPromptResponse other = (InsertPromptResponse) obj;
        return Objects.equals(commitId, other.commitId) && Objects.equals(parentCommitId, other.parentCommitId)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commitId, parentCommitId, createdAt, Objects.hashCode(additionalProperties));
    }
}