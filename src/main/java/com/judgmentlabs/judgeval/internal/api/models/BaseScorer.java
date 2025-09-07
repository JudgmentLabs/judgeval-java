package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseScorer {
    @JsonProperty("score_type")
    @Nonnull
    private String scoreType;

    @JsonProperty("threshold")
    @Nullable
    private Double threshold;

    @JsonProperty("name")
    @Nullable
    private String name;

    @JsonProperty("class_name")
    @Nullable
    private String className;

    @JsonProperty("score")
    @Nullable
    private Double score;

    @JsonProperty("score_breakdown")
    @Nullable
    private Object scoreBreakdown;

    @JsonProperty("reason")
    @Nullable
    private String reason;

    @JsonProperty("using_native_model")
    @Nullable
    private Boolean usingNativeModel;

    @JsonProperty("success")
    @Nullable
    private Boolean success;

    @JsonProperty("model")
    @Nullable
    private String model;

    @JsonProperty("model_client")
    @Nullable
    private Object modelClient;

    @JsonProperty("strict_mode")
    @Nullable
    private Boolean strictMode;

    @JsonProperty("error")
    @Nullable
    private String error;

    @JsonProperty("additional_metadata")
    @Nullable
    private Object additionalMetadata;

    @JsonProperty("user")
    @Nullable
    private String user;

    @JsonProperty("server_hosted")
    @Nullable
    private Boolean serverHosted;

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
    public String getScoreType() {
        return scoreType;
    }

    @Nullable
    public Double getThreshold() {
        return threshold;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getClassName() {
        return className;
    }

    @Nullable
    public Double getScore() {
        return score;
    }

    @Nullable
    public Object getScoreBreakdown() {
        return scoreBreakdown;
    }

    @Nullable
    public String getReason() {
        return reason;
    }

    @Nullable
    public Boolean getUsingNativeModel() {
        return usingNativeModel;
    }

    @Nullable
    public Boolean getSuccess() {
        return success;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    @Nullable
    public Object getModelClient() {
        return modelClient;
    }

    @Nullable
    public Boolean getStrictMode() {
        return strictMode;
    }

    @Nullable
    public String getError() {
        return error;
    }

    @Nullable
    public Object getAdditionalMetadata() {
        return additionalMetadata;
    }

    @Nullable
    public String getUser() {
        return user;
    }

    @Nullable
    public Boolean getServerHosted() {
        return serverHosted;
    }

    public void setScoreType(@Nonnull String scoreType) {
        this.scoreType = scoreType;
    }

    public void setThreshold(@Nullable Double threshold) {
        this.threshold = threshold;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setClassName(@Nullable String className) {
        this.className = className;
    }

    public void setScore(@Nullable Double score) {
        this.score = score;
    }

    public void setScoreBreakdown(@Nullable Object scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

    public void setUsingNativeModel(@Nullable Boolean usingNativeModel) {
        this.usingNativeModel = usingNativeModel;
    }

    public void setSuccess(@Nullable Boolean success) {
        this.success = success;
    }

    public void setModel(@Nullable String model) {
        this.model = model;
    }

    public void setModelClient(@Nullable Object modelClient) {
        this.modelClient = modelClient;
    }

    public void setStrictMode(@Nullable Boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setError(@Nullable String error) {
        this.error = error;
    }

    public void setAdditionalMetadata(@Nullable Object additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public void setUser(@Nullable String user) {
        this.user = user;
    }

    public void setServerHosted(@Nullable Boolean serverHosted) {
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
        return Objects.hash(
                scoreType,
                threshold,
                name,
                className,
                score,
                scoreBreakdown,
                reason,
                usingNativeModel,
                success,
                model,
                modelClient,
                strictMode,
                error,
                additionalMetadata,
                user,
                serverHosted,
                Objects.hashCode(additionalProperties));
    }
}
