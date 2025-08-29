package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
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

    @JsonProperty("depth")
    private Integer depth;

    @JsonProperty("created_at")
    private Object createdAt;

    @JsonProperty("parent_span_id")
    private Object parentSpanId;

    @JsonProperty("span_type")
    private Object spanType;

    @JsonProperty("inputs")
    private Object inputs;

    @JsonProperty("error")
    private Object error;

    @JsonProperty("output")
    private Object output;

    @JsonProperty("usage")
    private Object usage;

    @JsonProperty("duration")
    private Object duration;

    @JsonProperty("expected_tools")
    private Object expectedTools;

    @JsonProperty("additional_metadata")
    private Object additionalMetadata;

    @JsonProperty("has_evaluation")
    private Object hasEvaluation;

    @JsonProperty("agent_name")
    private Object agentName;

    @JsonProperty("class_name")
    private Object className;

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

    public Integer getDepth() {
        return depth;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public Object getParentSpanId() {
        return parentSpanId;
    }

    public Object getSpanType() {
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

    public Object getUsage() {
        return usage;
    }

    public Object getDuration() {
        return duration;
    }

    public Object getExpectedTools() {
        return expectedTools;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public Object getHasEvaluation() {
        return hasEvaluation;
    }

    public Object getAgentName() {
        return agentName;
    }

    public Object getClassName() {
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

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public void setParentSpanId(Object parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public void setSpanType(Object spanType) {
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

    public void setUsage(Object usage) {
        this.usage = usage;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public void setExpectedTools(Object expectedTools) {
        this.expectedTools = expectedTools;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setHasEvaluation(Object hasEvaluation) {
        this.hasEvaluation = hasEvaluation;
    }

    public void setAgentName(Object agentName) {
        this.agentName = agentName;
    }

    public void setClassName(Object className) {
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
                && Objects.equals(depth, other.depth)
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
                depth,
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
