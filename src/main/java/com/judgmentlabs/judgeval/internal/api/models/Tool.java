package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tool {
    @JsonProperty("tool_name")
    private String toolName;

    @JsonProperty("parameters")
    private Object parameters;

    @JsonProperty("agent_name")
    private String agentName;

    @JsonProperty("result_dependencies")
    private List<Object> resultDependencies;

    @JsonProperty("action_dependencies")
    private List<Object> actionDependencies;

    @JsonProperty("require_all")
    private Boolean requireAll;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getToolName() {
        return toolName;
    }

    public Object getParameters() {
        return parameters;
    }

    public String getAgentName() {
        return agentName;
    }

    public List<Object> getResultDependencies() {
        return resultDependencies;
    }

    public List<Object> getActionDependencies() {
        return actionDependencies;
    }

    public Boolean getRequireAll() {
        return requireAll;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setResultDependencies(List<Object> resultDependencies) {
        this.resultDependencies = resultDependencies;
    }

    public void setActionDependencies(List<Object> actionDependencies) {
        this.actionDependencies = actionDependencies;
    }

    public void setRequireAll(Boolean requireAll) {
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
