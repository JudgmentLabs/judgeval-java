package com.judgmentlabs.judgeval.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class ScoringResult {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("scorers_data")
    private Object scorersData;
    @JsonProperty("name")
    private Object name;
    @JsonProperty("data_object")
    private Object dataObject;
    @JsonProperty("trace_id")
    private Object traceId;
    @JsonProperty("run_duration")
    private Object runDuration;
    @JsonProperty("evaluation_cost")
    private Object evaluationCost;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public Boolean getSuccess() {
        return success;
    }
    public Object getScorersData() {
        return scorersData;
    }
    public Object getName() {
        return name;
    }
    public Object getDataObject() {
        return dataObject;
    }
    public Object getTraceId() {
        return traceId;
    }
    public Object getRunDuration() {
        return runDuration;
    }
    public Object getEvaluationCost() {
        return evaluationCost;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public void setScorersData(Object scorersData) {
        this.scorersData = scorersData;
    }
    public void setName(Object name) {
        this.name = name;
    }
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }
    public void setTraceId(Object traceId) {
        this.traceId = traceId;
    }
    public void setRunDuration(Object runDuration) {
        this.runDuration = runDuration;
    }
    public void setEvaluationCost(Object evaluationCost) {
        this.evaluationCost = evaluationCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScoringResult other = (ScoringResult) obj;
        return Objects.equals(success, other.success) && Objects.equals(scorersData, other.scorersData) && Objects.equals(name, other.name) && Objects.equals(dataObject, other.dataObject) && Objects.equals(traceId, other.traceId) && Objects.equals(runDuration, other.runDuration) && Objects.equals(evaluationCost, other.evaluationCost) && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(success) + Objects.hashCode(scorersData) + Objects.hashCode(name) + Objects.hashCode(dataObject) + Objects.hashCode(traceId) + Objects.hashCode(runDuration) + Objects.hashCode(evaluationCost) + Objects.hashCode(additionalProperties);
    }
}