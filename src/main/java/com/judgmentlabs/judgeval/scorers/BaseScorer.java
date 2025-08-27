package com.judgmentlabs.judgeval.scorers;

import java.util.Map;

public class BaseScorer {
    private String scoreType;
    private double threshold = 0.5;
    private String name;
    private String className;
    private Double score;
    private Map<String, Object> scoreBreakdown;
    private String reason = "";
    private Boolean usingNativeModel;
    private Boolean success;
    private String model;
    private Object modelClient;
    private boolean strictMode = false;
    private String error;
    private Map<String, Object> additionalMetadata;
    private String user;
    private boolean serverHosted = false;

    public BaseScorer() {
        this.className = this.getClass().getSimpleName();
        if (this.name == null) {
            this.name = this.className;
        }
        if (this.strictMode) {
            this.threshold = 1.0;
        }
    }

    public void addModel(String model) {
        // TODO: Implement model client creation
        this.model = model;
    }

    public boolean successCheck() {
        if (this.error != null) {
            return false;
        }
        if (this.score == null) {
            return false;
        }
        return this.score >= this.threshold;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Map<String, Object> getScoreBreakdown() {
        return scoreBreakdown;
    }

    public void setScoreBreakdown(Map<String, Object> scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getUsingNativeModel() {
        return usingNativeModel;
    }

    public void setUsingNativeModel(Boolean usingNativeModel) {
        this.usingNativeModel = usingNativeModel;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Object getModelClient() {
        return modelClient;
    }

    public void setModelClient(Object modelClient) {
        this.modelClient = modelClient;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Object> getAdditionalMetadata() {
        return additionalMetadata;
    }

    public void setAdditionalMetadata(Map<String, Object> additionalMetadata) {
        this.additionalMetadata = additionalMetadata;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isServerHosted() {
        return serverHosted;
    }

    public void setServerHosted(boolean serverHosted) {
        this.serverHosted = serverHosted;
    }
}
