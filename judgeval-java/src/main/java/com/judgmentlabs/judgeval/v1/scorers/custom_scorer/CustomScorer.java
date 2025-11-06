package com.judgmentlabs.judgeval.v1.scorers.custom_scorer;

import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;
import com.judgmentlabs.judgeval.v1.data.APIScorerType;
import com.judgmentlabs.judgeval.v1.scorers.APIScorer;

public final class CustomScorer extends APIScorer {
    private CustomScorer(Builder builder) {
        super(APIScorerType.CUSTOM);
        setName(builder.name);
        setClassName(builder.className);
        setServerHosted(builder.serverHosted);
    }

    @Override
    public ScorerConfig getScorerConfig() {
        throw new UnsupportedOperationException("CustomScorer does not use ScorerConfig");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String  name;
        private String  className;
        private boolean serverHosted;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder serverHosted(boolean serverHosted) {
            this.serverHosted = serverHosted;
            return this;
        }

        public CustomScorer build() {
            return new CustomScorer(this);
        }
    }
}
