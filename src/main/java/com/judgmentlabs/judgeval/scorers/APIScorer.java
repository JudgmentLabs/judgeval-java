package com.judgmentlabs.judgeval.scorers;

import java.util.List;

import com.judgmentlabs.judgeval.data.APIScorerType;

public class APIScorer extends BaseScorer {
    private APIScorerType scoreType;
    private String name;
    private double threshold = 0.5;
    private boolean strictMode = false;
    private List<String> requiredParams;

    public APIScorer(APIScorerType scoreType) {
        this.scoreType = scoreType;
        this.name = scoreType.toString();
        this.requiredParams = new java.util.ArrayList<>();
    }

    public void setThreshold(double threshold) {
        if (threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException(
                    "Threshold must be between 0 and 1, got: " + threshold);
        }
        this.threshold = threshold;
    }

    public String getScoreType() {
        return scoreType.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getThreshold() {
        return threshold;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public List<String> getRequiredParams() {
        return requiredParams;
    }

    public void setRequiredParams(List<String> requiredParams) {
        this.requiredParams = requiredParams;
    }
}
