package com.judgmentlabs.judgeval.scorers;

import java.util.ArrayList;
import java.util.List;

import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;

public abstract class BaseScorer extends com.judgmentlabs.judgeval.internal.api.models.BaseScorer {

    public BaseScorer() {
        super();
        if (getName() == null) {
            setName(this.getClass().getSimpleName());
        }
        if (Boolean.TRUE.equals(getStrictMode())) {
            setThreshold(1.0);
        }
    }

    public abstract ScorerConfig getScorerConfig();

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

    public String getModel() {
        Object model = super.getModel();
        return model != null ? model.toString() : null;
    }

    public void setModel(String model) {
        super.setModel(model);
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
}
