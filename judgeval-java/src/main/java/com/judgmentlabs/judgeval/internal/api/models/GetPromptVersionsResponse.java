package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPromptVersionsResponse {
    @JsonProperty("versions")
    private List<PromptCommitInfo> versions;

    private Map<String, Object>    additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public List<PromptCommitInfo> getVersions() {
        return versions;
    }

    public void setVersions(List<PromptCommitInfo> versions) {
        this.versions = versions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        GetPromptVersionsResponse other = (GetPromptVersionsResponse) obj;
        return Objects.equals(versions, other.versions)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versions, Objects.hashCode(additionalProperties));
    }
}