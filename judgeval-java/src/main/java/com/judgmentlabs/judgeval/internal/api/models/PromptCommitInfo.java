package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PromptCommitInfo {
    @JsonProperty("name")
    private String              name;
    @JsonProperty("prompt")
    private String              prompt;
    @JsonProperty("tags")
    private List<String>        tags;
    @JsonProperty("commit_id")
    private String              commitId;
    @JsonProperty("parent_commit_id")
    private String              parentCommitId;
    @JsonProperty("created_at")
    private String              createdAt;
    @JsonProperty("first_name")
    private String              firstName;
    @JsonProperty("last_name")
    private String              lastName;
    @JsonProperty("user_email")
    private String              userEmail;

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

    public String getPrompt() {
        return prompt;
    }

    public List<String> getTags() {
        return tags;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PromptCommitInfo other = (PromptCommitInfo) obj;
        return Objects.equals(name, other.name) && Objects.equals(prompt, other.prompt)
                && Objects.equals(tags, other.tags) && Objects.equals(commitId, other.commitId)
                && Objects.equals(parentCommitId, other.parentCommitId) && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
                && Objects.equals(userEmail, other.userEmail)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, prompt, tags, commitId, parentCommitId, createdAt, firstName, lastName, userEmail,
                Objects.hashCode(additionalProperties));
    }
}