package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseScorer {
    @JsonProperty("score_type")
    private String              scoreType;
    @JsonProperty("name")
    private String              name;
    @JsonProperty("class_name")
    private String              className;
    @JsonProperty("score")
    private Double              score;
    @JsonProperty("minimum_score_range")
    private Double              minimumScoreRange;
    @JsonProperty("maximum_score_range")
    private Double              maximumScoreRange;
    @JsonProperty("score_breakdown")
    private Object              scoreBreakdown;
    @JsonProperty("reason")
    private Object              reason;
    @JsonProperty("success")
    private Boolean             success;
    @JsonProperty("model")
    private String              model;
    @JsonProperty("error")
    private String              error;
    @JsonProperty("additional_metadata")
    private Object              additionalMetadata;
    @JsonProperty("user")
    private String              user;
    @JsonProperty("server_hosted")
    private Boolean             serverHosted;
    @JsonProperty("using_native_model")
    private Boolean             usingNativeModel;
    @JsonProperty("required_params")
    private List<String>        requiredParams;
    @JsonProperty("strict_mode")
    private Boolean             strictMode;

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

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public Double getScore() {
        return score;
    }

    public Double getMinimumScoreRange() {
        return minimumScoreRange;
    }

    public Double getMaximumScoreRange() {
        return maximumScoreRange;
    }

    public Object getScoreBreakdown() {
        return scoreBreakdown;
    }

    public Object getReason() {
        return reason;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getModel() {
        return model;
    }

    public String getError() {
        return error;
    }

    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    public String getUser() {
        return user;
    }

    public Boolean getServerHosted() {
        return serverHosted;
    }

    public Boolean getUsingNativeModel() {
        return usingNativeModel;
    }

    public List<String> getRequiredParams() {
        return requiredParams;
    }

    public Boolean getStrictMode() {
        return strictMode;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setMinimumScoreRange(Double minimumScoreRange) {
        this.minimumScoreRange = minimumScoreRange;
    }

    public void setMaximumScoreRange(Double maximumScoreRange) {
        this.maximumScoreRange = maximumScoreRange;
    }

    public void setScoreBreakdown(Object scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setAdditionalMetadata(Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setServerHosted(Boolean serverHosted) {
        this.serverHosted = serverHosted;
    }

    public void setUsingNativeModel(Boolean usingNativeModel) {
        this.usingNativeModel = usingNativeModel;
    }

    public void setRequiredParams(List<String> requiredParams) {
        this.requiredParams = requiredParams;
    }

    public void setStrictMode(Boolean strictMode) {
        this.strictMode = strictMode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        BaseScorer other = (BaseScorer) obj;
        return Objects.equals(scoreType, other.scoreType) && Objects.equals(name, other.name)
                && Objects.equals(className, other.className) && Objects.equals(score, other.score)
                && Objects.equals(minimumScoreRange, other.minimumScoreRange)
                && Objects.equals(maximumScoreRange, other.maximumScoreRange)
                && Objects.equals(scoreBreakdown, other.scoreBreakdown) && Objects.equals(reason, other.reason)
                && Objects.equals(success, other.success) && Objects.equals(model, other.model)
                && Objects.equals(error, other.error) && Objects.equals(additionalMetadata, other.additionalMetadata)
                && Objects.equals(user, other.user) && Objects.equals(serverHosted, other.serverHosted)
                && Objects.equals(usingNativeModel, other.usingNativeModel)
                && Objects.equals(requiredParams, other.requiredParams) && Objects.equals(strictMode, other.strictMode)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreType, name, className, score, minimumScoreRange, maximumScoreRange, scoreBreakdown,
                reason, success, model, error, additionalMetadata, user, serverHosted, usingNativeModel, requiredParams,
                strictMode, Objects.hashCode(additionalProperties));
    }
}