package com.judgmentlabs.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalResultsFetch {
    @JsonProperty("experiment_run_id")
    private String experimentRunId;

    @JsonProperty("project_name")
    private String projectName;

    public String getExperimentRunId() {
        return experimentRunId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setExperimentRunId(String experimentRunId) {
        this.experimentRunId = experimentRunId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EvalResultsFetch other = (EvalResultsFetch) obj;
        return Objects.equals(experimentRunId, other.experimentRunId)
                && Objects.equals(projectName, other.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(experimentRunId) + Objects.hashCode(projectName);
    }
}
