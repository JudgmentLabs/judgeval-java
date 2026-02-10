package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TagPromptRequest {
    @JsonProperty("commit_id")
    private String              commitId;
    @JsonProperty("tags")
    private List<String>        tags;

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

    public List<String> getTags() {
        return tags;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TagPromptRequest other = (TagPromptRequest) obj;
        return Objects.equals(commitId, other.commitId) && Objects.equals(tags, other.tags)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commitId, tags, Objects.hashCode(additionalProperties));
    }
}