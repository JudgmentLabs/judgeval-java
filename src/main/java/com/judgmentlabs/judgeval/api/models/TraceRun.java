package com.judgmentlabs.judgeval.api.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceRun {
    @JsonProperty("project_name")
    private Object projectName;

    @JsonProperty("eval_name")
    private Object evalName;

    @JsonProperty("traces")
    private List<Trace> traces;

    @JsonProperty("scorers")
    private List<ScorerConfig> scorers;

    @JsonProperty("model")
    private String model;

    @JsonProperty("trace_span_id")
    private Object traceSpanId;

    @JsonProperty("tools")
    private Object tools;

    public Object getProjectName() {
        return projectName;
    }

    public Object getEvalName() {
        return evalName;
    }

    public List<Trace> getTraces() {
        return traces;
    }

    public List<ScorerConfig> getScorers() {
        return scorers;
    }

    public String getModel() {
        return model;
    }

    public Object getTraceSpanId() {
        return traceSpanId;
    }

    public Object getTools() {
        return tools;
    }

    public void setProjectName(Object projectName) {
        this.projectName = projectName;
    }

    public void setEvalName(Object evalName) {
        this.evalName = evalName;
    }

    public void setTraces(List<Trace> traces) {
        this.traces = traces;
    }

    public void setScorers(List<ScorerConfig> scorers) {
        this.scorers = scorers;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setTraceSpanId(Object traceSpanId) {
        this.traceSpanId = traceSpanId;
    }

    public void setTools(Object tools) {
        this.tools = tools;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TraceRun other = (TraceRun) obj;
        return Objects.equals(projectName, other.projectName)
                && Objects.equals(evalName, other.evalName)
                && Objects.equals(traces, other.traces)
                && Objects.equals(scorers, other.scorers)
                && Objects.equals(model, other.model)
                && Objects.equals(traceSpanId, other.traceSpanId)
                && Objects.equals(tools, other.tools);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectName)
                + Objects.hashCode(evalName)
                + Objects.hashCode(traces)
                + Objects.hashCode(scorers)
                + Objects.hashCode(model)
                + Objects.hashCode(traceSpanId)
                + Objects.hashCode(tools);
    }
}
