package com.judgmentlabs.judgeval.scorers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;

public abstract class BaseScorer extends com.judgmentlabs.judgeval.internal.api.models.BaseScorer {

    public BaseScorer() {
        super();
        setClassName(this.getClass().getSimpleName());
        if (getName() == null) {
            setName(getClassName());
        }
        if (Boolean.TRUE.equals(getStrictMode())) {
            setThreshold(1.0);
        }
    }

    public abstract ScorerConfig getScorerConfig();

    public void addModel(String model) {
        setModel(model);
    }

    public boolean successCheck() {
        if (getError() != null) {
            return false;
        }
        if (getScore() == null) {
            return false;
        }
        Double threshold = getThreshold();
        Double score = getScore();
        return threshold != null && score != null && score >= threshold;
    }

    public List<String> getRequiredParams() {
        return new ArrayList<>();
    }

    public String getScoreType() {
        Object scoreType = super.getScoreType();
        return scoreType != null ? scoreType.toString() : null;
    }

    public void setScoreType(String scoreType) {
        super.setScoreType(scoreType);
    }

    public Double getThreshold() {
        Double threshold = super.getThreshold();
        return threshold != null ? threshold : 0.5;
    }

    public void setThreshold(double threshold) {
        super.setThreshold(threshold);
    }

    public String getName() {
        Object name = super.getName();
        return name != null ? name.toString() : null;
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getClassName() {
        Object className = super.getClassName();
        return className != null ? className.toString() : null;
    }

    public void setClassName(String className) {
        super.setClassName(className);
    }

    public Double getScore() {
        Object score = super.getScore();
        if (score instanceof Number) {
            return ((Number) score).doubleValue();
        }
        return null;
    }

    public void setScore(Double score) {
        super.setScore(score);
    }

    public Map<String, Object> getScoreBreakdown() {
        Object breakdown = super.getScoreBreakdown();
        if (breakdown instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) breakdown;
            return map;
        }
        return null;
    }

    public void setScoreBreakdown(Map<String, Object> scoreBreakdown) {
        super.setScoreBreakdown(scoreBreakdown);
    }

    public String getReason() {
        Object reason = super.getReason();
        return reason != null ? reason.toString() : "";
    }

    public void setReason(String reason) {
        super.setReason(reason);
    }

    public Boolean getUsingNativeModel() {
        Object usingNativeModel = super.getUsingNativeModel();
        if (usingNativeModel instanceof Boolean) {
            return (Boolean) usingNativeModel;
        }
        return null;
    }

    public void setUsingNativeModel(Boolean usingNativeModel) {
        super.setUsingNativeModel(usingNativeModel);
    }

    public Boolean getSuccess() {
        Object success = super.getSuccess();
        if (success instanceof Boolean) {
            return (Boolean) success;
        }
        return null;
    }

    public void setSuccess(Boolean success) {
        super.setSuccess(success);
    }

    public String getModel() {
        Object model = super.getModel();
        return model != null ? model.toString() : null;
    }

    public void setModel(String model) {
        super.setModel(model);
    }

    public Object getModelClient() {
        return super.getModelClient();
    }

    public void setModelClient(Object modelClient) {
        super.setModelClient(modelClient);
    }

    public boolean isStrictMode() {
        Boolean strictMode = super.getStrictMode();
        return strictMode != null ? strictMode : false;
    }

    public void setStrictMode(boolean strictMode) {
        super.setStrictMode(strictMode);
    }

    public String getError() {
        Object error = super.getError();
        return error != null ? error.toString() : null;
    }

    public void setError(String error) {
        super.setError(error);
    }

    public Map<String, Object> getAdditionalMetadata() {
        Object metadata = super.getAdditionalMetadata();
        if (metadata instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) metadata;
            return map;
        }
        return null;
    }

    public void setAdditionalMetadata(Map<String, Object> additionalMetadata) {
        super.setAdditionalMetadata(additionalMetadata);
    }

    public String getUser() {
        Object user = super.getUser();
        return user != null ? user.toString() : null;
    }

    public void setUser(String user) {
        super.setUser(user);
    }

    public boolean isServerHosted() {
        Boolean serverHosted = super.getServerHosted();
        return serverHosted != null ? serverHosted : false;
    }

    public void setServerHosted(boolean serverHosted) {
        super.setServerHosted(serverHosted);
    }
}
