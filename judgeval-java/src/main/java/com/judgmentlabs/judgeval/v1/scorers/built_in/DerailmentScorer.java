package com.judgmentlabs.judgeval.v1.scorers.built_in;

import java.util.Arrays;

import com.judgmentlabs.judgeval.v1.data.APIScorerType;
import com.judgmentlabs.judgeval.v1.scorers.APIScorer;

/**
 * Scorer that detects whether a conversation has derailed from its intended
 * topic.
 */
public final class DerailmentScorer extends APIScorer {
    private DerailmentScorer(Builder builder) {
        super(APIScorerType.DERAILMENT);
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

    /**
     * Creates a new builder for configuring a DerailmentScorer.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring and creating DerailmentScorer instances.
     */
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

        public DerailmentScorer build() {
            return new DerailmentScorer(this);
        }
    }
}
