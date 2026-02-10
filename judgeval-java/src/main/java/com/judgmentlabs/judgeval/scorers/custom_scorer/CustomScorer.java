package com.judgmentlabs.judgeval.scorers.custom_scorer;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;
import com.judgmentlabs.judgeval.scorers.APIScorer;

/**
 * Scorer that uses custom user-defined evaluation logic. Scorers are hosted on
 * Judgment Servers
 * and can be uploaded using the judgeval CLI.
 * 
 * @see <a href="https://docs.judgment.ai/judgeval/cli/upload-scorers">Judgment
 *      Docs: Upload Scorers</a>
 */
public final class CustomScorer extends APIScorer {
    private final String projectId;

    private CustomScorer(Builder builder) {
        super(APIScorerType.CUSTOM);
        setName(builder.name);
        setClassName(builder.className);
        setServerHosted(true);
        this.projectId = builder.projectId;
    }

    @Override
    public ScorerConfig getScorerConfig() {
        throw new UnsupportedOperationException("CustomScorer does not use ScorerConfig");
    }

    public String getProjectId() {
        return projectId;
    }

    /**
     * Creates a new builder for configuring a CustomScorer.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring and creating CustomScorer instances.
     */
    public static final class Builder {
        private String name;
        private String className;
        private String projectId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public CustomScorer build() {
            return new CustomScorer(this);
        }
    }
}
