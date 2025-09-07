package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tool {
    @JsonProperty("tool_name")
    @Nonnull
    private String toolName;

    @JsonProperty("parameters")
    @Nullable
    private Object parameters;

    @JsonProperty("agent_name")
    @Nullable
    private String agentName;

    @JsonProperty("result_dependencies")
    @Nullable
    private List<Object> resultDependencies;

    @JsonProperty("action_dependencies")
    @Nullable
    private List<Object> actionDependencies;

    @JsonProperty("require_all")
    @Nullable
    private Boolean requireAll;

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
    public String getToolName() {
        return toolName;
    }

    @Nullable
    public Object getParameters() {
        return parameters;
    }

    @Nullable
    public String getAgentName() {
        return agentName;
    }

    @Nullable
    public List<Object> getResultDependencies() {
        return resultDependencies;
    }

    @Nullable
    public List<Object> getActionDependencies() {
        return actionDependencies;
    }

    @Nullable
    public Boolean getRequireAll() {
        return requireAll;
    }

    public void setToolName(@Nonnull String toolName) {
        this.toolName = toolName;
    }

    public void setParameters(@Nullable Object parameters) {
        this.parameters = parameters;
    }

    public void setAgentName(@Nullable String agentName) {
        this.agentName = agentName;
    }

    public void setResultDependencies(@Nullable List<Object> resultDependencies) {
        this.resultDependencies = resultDependencies;
    }

    public void setActionDependencies(@Nullable List<Object> actionDependencies) {
        this.actionDependencies = actionDependencies;
    }

    public void setRequireAll(@Nullable Boolean requireAll) {
        this.requireAll = requireAll;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tool other = (Tool) obj;
        return Objects.equals(toolName, other.toolName)
                && Objects.equals(parameters, other.parameters)
                && Objects.equals(agentName, other.agentName)
                && Objects.equals(resultDependencies, other.resultDependencies)
                && Objects.equals(actionDependencies, other.actionDependencies)
                && Objects.equals(requireAll, other.requireAll)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                toolName,
                parameters,
                agentName,
                resultDependencies,
                actionDependencies,
                requireAll,
                Objects.hashCode(additionalProperties));
    }
}
