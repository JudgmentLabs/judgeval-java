package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceSpan {
    @JsonProperty("span_id")
    private String spanId;

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("function")
    private String function;

    @JsonProperty("created_at")
    private Object createdAt;

    @JsonProperty("parent_span_id")
    private String parentSpanId;

    @JsonProperty("span_type")
    private String spanType;

    @JsonProperty("inputs")
    private Object inputs;

    @JsonProperty("error")
    private Object error;

    @JsonProperty("output")
    private Object output;

    @JsonProperty("usage")
    private TraceUsage usage;

    @JsonProperty("duration")
    private Double duration;

    @JsonProperty("expected_tools")
    private List<Tool> expectedTools;

    @JsonProperty("additional_metadata")
    private Object additionalMetadata;

    @JsonProperty("has_evaluation")
    private Boolean hasEvaluation;

    @JsonProperty("agent_name")
    private String agentName;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("state_before")
    private Object stateBefore;

    @JsonProperty("state_after")
    private Object stateAfter;

    @JsonProperty("update_id")
    private Integer updateId;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getSpanId() {
        return spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getFunction() {
        return function;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public String getSpanType() {
        return spanType;
    }

    public Object getInputs() {
        return inputs;
    }

    public Object getError() {
        return error;
    }

    public Object getOutput() {
        return output;
    }

    public TraceUsage getUsage() {
        return usage;
    }

    public Double getDuration() {
        return duration;
    }

    public List<Tool> getExpectedTools() {
        return expectedTools;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public Boolean getHasEvaluation() {
        return hasEvaluation;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getClassName() {
        return className;
    }

    public Object getStateBefore() {
        return stateBefore;
    }

    public Object getStateAfter() {
        return stateAfter;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public void setSpanType(String spanType) {
        this.spanType = spanType;
    }

    public void setInputs(Object inputs) {
        this.inputs = inputs;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public void setUsage(TraceUsage usage) {
        this.usage = usage;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setExpectedTools(List<Tool> expectedTools) {
        this.expectedTools = expectedTools;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setHasEvaluation(Boolean hasEvaluation) {
        this.hasEvaluation = hasEvaluation;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setStateBefore(Object stateBefore) {
        this.stateBefore = stateBefore;
    }

    public void setStateAfter(Object stateAfter) {
        this.stateAfter = stateAfter;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TraceSpan other = (TraceSpan) obj;
        return Objects.equals(spanId, other.spanId)
                && Objects.equals(traceId, other.traceId)
                && Objects.equals(function, other.function)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(parentSpanId, other.parentSpanId)
                && Objects.equals(spanType, other.spanType)
                && Objects.equals(inputs, other.inputs)
                && Objects.equals(error, other.error)
                && Objects.equals(output, other.output)
                && Objects.equals(usage, other.usage)
                && Objects.equals(duration, other.duration)
                && Objects.equals(expectedTools, other.expectedTools)
                && Objects.equals(additionalMetadata, other.additionalMetadata)
                && Objects.equals(hasEvaluation, other.hasEvaluation)
                && Objects.equals(agentName, other.agentName)
                && Objects.equals(className, other.className)
                && Objects.equals(stateBefore, other.stateBefore)
                && Objects.equals(stateAfter, other.stateAfter)
                && Objects.equals(updateId, other.updateId)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                spanId,
                traceId,
                function,
                createdAt,
                parentSpanId,
                spanType,
                inputs,
                error,
                output,
                usage,
                duration,
                expectedTools,
                additionalMetadata,
                hasEvaluation,
                agentName,
                className,
                stateBefore,
                stateAfter,
                updateId,
                Objects.hashCode(additionalProperties));
    }
}
