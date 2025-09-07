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

public class TraceSpan {
    @JsonProperty("span_id")
    @Nonnull
    private String spanId;

    @JsonProperty("trace_id")
    @Nonnull
    private String traceId;

    @JsonProperty("function")
    @Nonnull
    private String function;

    @JsonProperty("created_at")
    @Nullable
    private Object createdAt;

    @JsonProperty("parent_span_id")
    @Nullable
    private String parentSpanId;

    @JsonProperty("span_type")
    @Nullable
    private String spanType;

    @JsonProperty("inputs")
    @Nullable
    private Object inputs;

    @JsonProperty("error")
    @Nullable
    private Object error;

    @JsonProperty("output")
    @Nullable
    private Object output;

    @JsonProperty("usage")
    @Nullable
    private TraceUsage usage;

    @JsonProperty("duration")
    @Nullable
    private Double duration;

    @JsonProperty("expected_tools")
    @Nullable
    private List<Tool> expectedTools;

    @JsonProperty("additional_metadata")
    @Nullable
    private Object additionalMetadata;

    @JsonProperty("has_evaluation")
    @Nullable
    private Boolean hasEvaluation;

    @JsonProperty("agent_name")
    @Nullable
    private String agentName;

    @JsonProperty("class_name")
    @Nullable
    private String className;

    @JsonProperty("state_before")
    @Nullable
    private Object stateBefore;

    @JsonProperty("state_after")
    @Nullable
    private Object stateAfter;

    @JsonProperty("update_id")
    @Nullable
    private Integer updateId;

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
    public String getSpanId() {
        return spanId;
    }

    @Nonnull
    public String getTraceId() {
        return traceId;
    }

    @Nonnull
    public String getFunction() {
        return function;
    }

    @Nullable
    public Object getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public String getParentSpanId() {
        return parentSpanId;
    }

    @Nullable
    public String getSpanType() {
        return spanType;
    }

    @Nullable
    public Object getInputs() {
        return inputs;
    }

    @Nullable
    public Object getError() {
        return error;
    }

    @Nullable
    public Object getOutput() {
        return output;
    }

    @Nullable
    public TraceUsage getUsage() {
        return usage;
    }

    @Nullable
    public Double getDuration() {
        return duration;
    }

    @Nullable
    public List<Tool> getExpectedTools() {
        return expectedTools;
    }

    @Nullable
    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    @Nullable
    public Boolean getHasEvaluation() {
        return hasEvaluation;
    }

    @Nullable
    public String getAgentName() {
        return agentName;
    }

    @Nullable
    public String getClassName() {
        return className;
    }

    @Nullable
    public Object getStateBefore() {
        return stateBefore;
    }

    @Nullable
    public Object getStateAfter() {
        return stateAfter;
    }

    @Nullable
    public Integer getUpdateId() {
        return updateId;
    }

    public void setSpanId(@Nonnull String spanId) {
        this.spanId = spanId;
    }

    public void setTraceId(@Nonnull String traceId) {
        this.traceId = traceId;
    }

    public void setFunction(@Nonnull String function) {
        this.function = function;
    }

    public void setCreatedAt(@Nullable Object createdAt) {
        this.createdAt = createdAt;
    }

    public void setParentSpanId(@Nullable String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public void setSpanType(@Nullable String spanType) {
        this.spanType = spanType;
    }

    public void setInputs(@Nullable Object inputs) {
        this.inputs = inputs;
    }

    public void setError(@Nullable Object error) {
        this.error = error;
    }

    public void setOutput(@Nullable Object output) {
        this.output = output;
    }

    public void setUsage(@Nullable TraceUsage usage) {
        this.usage = usage;
    }

    public void setDuration(@Nullable Double duration) {
        this.duration = duration;
    }

    public void setExpectedTools(@Nullable List<Tool> expectedTools) {
        this.expectedTools = expectedTools;
    }

    public void setAdditionalMetadata(@Nullable Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setHasEvaluation(@Nullable Boolean hasEvaluation) {
        this.hasEvaluation = hasEvaluation;
    }

    public void setAgentName(@Nullable String agentName) {
        this.agentName = agentName;
    }

    public void setClassName(@Nullable String className) {
        this.className = className;
    }

    public void setStateBefore(@Nullable Object stateBefore) {
        this.stateBefore = stateBefore;
    }

    public void setStateAfter(@Nullable Object stateAfter) {
        this.stateAfter = stateAfter;
    }

    public void setUpdateId(@Nullable Integer updateId) {
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
