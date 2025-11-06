package com.judgmentlabs.judgeval.v1.scorers.built_in;

import java.util.Arrays;

import com.judgmentlabs.judgeval.v1.data.APIScorerType;
import com.judgmentlabs.judgeval.v1.scorers.APIScorer;

public final class AnswerRelevancyScorer extends APIScorer {
    private AnswerRelevancyScorer(Builder builder) {
        super(APIScorerType.ANSWER_RELEVANCY);
        setRequiredParams(Arrays.asList("input", "actual_output"));
        if (builder.threshold >= 0) {
            setThreshold(builder.threshold);
        }
        if (builder.name != null) {
            setName(builder.name);
        }
        if (builder.strictMode != null) {
            setStrictMode(builder.strictMode);
        }
        if (builder.model != null) {
            setModel(builder.model);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private double  threshold = -1;
        private String  name;
        private Boolean strictMode;
        private String  model;

        public Builder threshold(double threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder strictMode(boolean strictMode) {
            this.strictMode = strictMode;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public AnswerRelevancyScorer build() {
            return new AnswerRelevancyScorer(this);
        }
    }
}
