package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleEvaluationRun {
    @JsonProperty("id")
    private String              id;
    @JsonProperty("project_id")
    private String              projectId;
    @JsonProperty("eval_name")
    private String              evalName;
    @JsonProperty("model")
    private String              model;
    @JsonProperty("created_at")
    private String              createdAt;
    @JsonProperty("user_id")
    private String              userId;
    @JsonProperty("scorers")
    private List<Object>        scorers;
    @JsonProperty("custom_scorers")
    private List<BaseScorer>    customScorers;
    @JsonProperty("judgment_scorers")
    private List<ScorerConfig>  judgmentScorers;
    @JsonProperty("examples")
    private List<Example>       examples;
    @JsonProperty("trace_span_id")
    private String              traceSpanId;
    @JsonProperty("trace_id")
    private String              traceId;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getEvalName() {
        return evalName;
    }

    public String getModel() {
        return model;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public List<Object> getScorers() {
        return scorers;
    }

    public List<BaseScorer> getCustomScorers() {
        return customScorers;
    }

    public List<ScorerConfig> getJudgmentScorers() {
        return judgmentScorers;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public String getTraceSpanId() {
        return traceSpanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setEvalName(String evalName) {
        this.evalName = evalName;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setScorers(List<Object> scorers) {
        this.scorers = scorers;
    }

    public void setCustomScorers(List<BaseScorer> customScorers) {
        this.customScorers = customScorers;
    }

    public void setJudgmentScorers(List<ScorerConfig> judgmentScorers) {
        this.judgmentScorers = judgmentScorers;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public void setTraceSpanId(String traceSpanId) {
        this.traceSpanId = traceSpanId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ExampleEvaluationRun other = (ExampleEvaluationRun) obj;
        return Objects.equals(id, other.id) && Objects.equals(projectId, other.projectId)
                && Objects.equals(evalName, other.evalName) && Objects.equals(model, other.model)
                && Objects.equals(createdAt, other.createdAt) && Objects.equals(userId, other.userId)
                && Objects.equals(scorers, other.scorers) && Objects.equals(customScorers, other.customScorers)
                && Objects.equals(judgmentScorers, other.judgmentScorers) && Objects.equals(examples, other.examples)
                && Objects.equals(traceSpanId, other.traceSpanId) && Objects.equals(traceId, other.traceId)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, evalName, model, createdAt, userId, scorers, customScorers, judgmentScorers,
                examples, traceSpanId, traceId, Objects.hashCode(additionalProperties));
    }
}