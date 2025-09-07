package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalResultsFetch {
    @JsonProperty("experiment_run_id")
    @Nonnull
    private String experimentRunId;

    @JsonProperty("project_name")
    @Nonnull
    private String projectName;

    @Nonnull private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    @Nonnull
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(@Nonnull String name, @Nullable Object value) {
        additionalProperties.put(name, value);
    }

    @Nonnull
    public String getExperimentRunId() {
        return experimentRunId;
    }

    @Nonnull
    public String getProjectName() {
        return projectName;
    }

    public void setExperimentRunId(@Nonnull String experimentRunId) {
        this.experimentRunId = experimentRunId;
    }

    public void setProjectName(@Nonnull String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EvalResultsFetch other = (EvalResultsFetch) obj;
        return Objects.equals(experimentRunId, other.experimentRunId)
                && Objects.equals(projectName, other.projectName)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(experimentRunId, projectName, Objects.hashCode(additionalProperties));
    }
}
