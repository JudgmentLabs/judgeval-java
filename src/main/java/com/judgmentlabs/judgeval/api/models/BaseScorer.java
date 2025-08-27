package com.judgmentlabs.judgeval.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseScorer {
    @JsonProperty("score_type")
    private String scoreType;

    @JsonProperty("threshold")
    private Double threshold;

    @JsonProperty("name")
    private Object name;

    @JsonProperty("class_name")
    private Object className;

    @JsonProperty("score")
    private Object score;

    @JsonProperty("score_breakdown")
    private Object scoreBreakdown;

    @JsonProperty("reason")
    private Object reason;

    @JsonProperty("using_native_model")
    private Object usingNativeModel;

    @JsonProperty("success")
    private Object success;

    @JsonProperty("model")
    private Object model;

    @JsonProperty("model_client")
    private Object modelClient;

    @JsonProperty("strict_mode")
    private Boolean strictMode;

    @JsonProperty("error")
    private Object error;

    @JsonProperty("additional_metadata")
    private Object additionalMetadata;

    @JsonProperty("user")
    private Object user;

    @JsonProperty("server_hosted")
    private Boolean serverHosted;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getScoreType() {
        return scoreType;
    }

    public Double getThreshold() {
        return threshold;
    }

    public Object getName() {
        return name;
    }

    public Object getClassName() {
        return className;
    }

    public Object getScore() {
        return score;
    }

    public Object getScoreBreakdown() {
        return scoreBreakdown;
    }

    public Object getReason() {
        return reason;
    }

    public Object getUsingNativeModel() {
        return usingNativeModel;
    }

    public Object getSuccess() {
        return success;
    }

    public Object getModel() {
        return model;
    }

    public Object getModelClient() {
        return modelClient;
    }

    public Boolean getStrictMode() {
        return strictMode;
    }

    public Object getError() {
        return error;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public Object getUser() {
        return user;
    }

    public Boolean getServerHosted() {
        return serverHosted;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public void setClassName(Object className) {
        this.className = className;
    }

    public void setScore(Object score) {
        this.score = score;
    }

    public void setScoreBreakdown(Object scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public void setUsingNativeModel(Object usingNativeModel) {
        this.usingNativeModel = usingNativeModel;
    }

    public void setSuccess(Object success) {
        this.success = success;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public void setModelClient(Object modelClient) {
        this.modelClient = modelClient;
    }

    public void setStrictMode(Boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public void setServerHosted(Boolean serverHosted) {
        this.serverHosted = serverHosted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseScorer other = (BaseScorer) obj;
        return Objects.equals(scoreType, other.scoreType)
                && Objects.equals(threshold, other.threshold)
                && Objects.equals(name, other.name)
                && Objects.equals(className, other.className)
                && Objects.equals(score, other.score)
                && Objects.equals(scoreBreakdown, other.scoreBreakdown)
                && Objects.equals(reason, other.reason)
                && Objects.equals(usingNativeModel, other.usingNativeModel)
                && Objects.equals(success, other.success)
                && Objects.equals(model, other.model)
                && Objects.equals(modelClient, other.modelClient)
                && Objects.equals(strictMode, other.strictMode)
                && Objects.equals(error, other.error)
                && Objects.equals(additionalMetadata, other.additionalMetadata)
                && Objects.equals(user, other.user)
                && Objects.equals(serverHosted, other.serverHosted)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scoreType)
                + Objects.hashCode(threshold)
                + Objects.hashCode(name)
                + Objects.hashCode(className)
                + Objects.hashCode(score)
                + Objects.hashCode(scoreBreakdown)
                + Objects.hashCode(reason)
                + Objects.hashCode(usingNativeModel)
                + Objects.hashCode(success)
                + Objects.hashCode(model)
                + Objects.hashCode(modelClient)
                + Objects.hashCode(strictMode)
                + Objects.hashCode(error)
                + Objects.hashCode(additionalMetadata)
                + Objects.hashCode(user)
                + Objects.hashCode(serverHosted)
                + Objects.hashCode(additionalProperties);
    }
}
