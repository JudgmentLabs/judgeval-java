package com.judgmentlabs.judgeval.scorers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.judgmentlabs.judgeval.api.models.ScorerConfig;
import com.judgmentlabs.judgeval.data.APIScorerType;

public class APIScorer extends BaseScorer {
    private APIScorerType scoreType;

    @JsonIgnore
    private List<String> requiredParams;

    public APIScorer(APIScorerType scoreType) {
        super();
        this.scoreType = scoreType;
        setName(scoreType.toString());
        setScoreType(scoreType.toString());
        this.requiredParams = new java.util.ArrayList<>();
    }

    public void setThreshold(double threshold) {
        if (threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException(
                    "Threshold must be between 0 and 1, got: " + threshold);
        }
        super.setThreshold(threshold);
    }

    public String getScoreType() {
        return scoreType.toString();
    }

    public List<String> getRequiredParams() {
        return requiredParams;
    }

    public void setRequiredParams(List<String> requiredParams) {
        this.requiredParams = requiredParams;
    }

    @Override
    public Object toTransport() {
        ScorerConfig cfg = new ScorerConfig();
        cfg.setScoreType(getScoreType());
        cfg.setThreshold(getThreshold());
        cfg.setName(getName());
        cfg.setStrictMode(isStrictMode());
        cfg.setRequiredParams(getRequiredParams());
        Map<String, Object> kwargs = new HashMap<>();
        if (getAdditionalProperties() != null)
            kwargs.putAll(getAdditionalProperties());
        cfg.setKwargs(kwargs);
        return cfg;
    }

    public static <T extends APIScorer> Builder<T> builder(Class<T> scorerClass) {
        return new Builder<>(scorerClass);
    }

    public static final class Builder<T extends APIScorer> {
        private final T scorer;

        private Builder(Class<T> scorerClass) {
            try {
                this.scorer = scorerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create scorer instance", e);
            }
        }

        public Builder<T> threshold(double threshold) {
            scorer.setThreshold(threshold);
            return this;
        }

        public Builder<T> name(String name) {
            scorer.setName(name);
            return this;
        }

        public Builder<T> strictMode(boolean strictMode) {
            scorer.setStrictMode(strictMode);
            return this;
        }

        public Builder<T> requiredParams(List<String> requiredParams) {
            scorer.setRequiredParams(requiredParams);
            return this;
        }

        public Builder<T> model(String model) {
            scorer.setModel(model);
            return this;
        }

        public Builder<T> additionalProperty(String key, Object value) {
            scorer.setAdditionalProperty(key, value);
            return this;
        }

        public T build() {
            return scorer;
        }
    }
}
