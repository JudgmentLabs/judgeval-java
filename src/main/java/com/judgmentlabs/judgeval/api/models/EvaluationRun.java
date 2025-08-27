package com.judgmentlabs.judgeval.api.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationRun {
    @JsonProperty("id")
    private Object id;

    @JsonProperty("project_name")
    private Object projectName;

    @JsonProperty("eval_name")
    private Object evalName;

    @JsonProperty("examples")
    private List<Example> examples;

    @JsonProperty("custom_scorers")
    private List<BaseScorer> customScorers;

    @JsonProperty("judgment_scorers")
    private List<ScorerConfig> judgmentScorers;

    @JsonProperty("model")
    private String model;

    @JsonProperty("trace_span_id")
    private Object traceSpanId;

    @JsonProperty("trace_id")
    private Object traceId;

    @JsonProperty("created_at")
    private Object createdAt;

    public Object getId() {
        return id;
    }

    public Object getProjectName() {
        return projectName;
    }

    public Object getEvalName() {
        return evalName;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public List<BaseScorer> getCustomScorers() {
        return customScorers;
    }

    public List<ScorerConfig> getJudgmentScorers() {
        return judgmentScorers;
    }

    public String getModel() {
        return model;
    }

    public Object getTraceSpanId() {
        return traceSpanId;
    }

    public Object getTraceId() {
        return traceId;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public void setProjectName(Object projectName) {
        this.projectName = projectName;
    }

    public void setEvalName(Object evalName) {
        this.evalName = evalName;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public void setCustomScorers(List<BaseScorer> customScorers) {
        this.customScorers = customScorers;
    }

    public void setJudgmentScorers(List<ScorerConfig> judgmentScorers) {
        this.judgmentScorers = judgmentScorers;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setTraceSpanId(Object traceSpanId) {
        this.traceSpanId = traceSpanId;
    }

    public void setTraceId(Object traceId) {
        this.traceId = traceId;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EvaluationRun other = (EvaluationRun) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(projectName, other.projectName)
                && Objects.equals(evalName, other.evalName)
                && Objects.equals(examples, other.examples)
                && Objects.equals(customScorers, other.customScorers)
                && Objects.equals(judgmentScorers, other.judgmentScorers)
                && Objects.equals(model, other.model)
                && Objects.equals(traceSpanId, other.traceSpanId)
                && Objects.equals(traceId, other.traceId)
                && Objects.equals(createdAt, other.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id)
                + Objects.hashCode(projectName)
                + Objects.hashCode(evalName)
                + Objects.hashCode(examples)
                + Objects.hashCode(customScorers)
                + Objects.hashCode(judgmentScorers)
                + Objects.hashCode(model)
                + Objects.hashCode(traceSpanId)
                + Objects.hashCode(traceId)
                + Objects.hashCode(createdAt);
    }
}
