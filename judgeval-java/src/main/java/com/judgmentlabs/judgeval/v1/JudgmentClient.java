package com.judgmentlabs.judgeval.v1;

import java.util.Objects;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.v1.evaluation.EvaluationFactory;
import com.judgmentlabs.judgeval.v1.scorers.ScorersFactory;
import com.judgmentlabs.judgeval.v1.tracer.TracerFactory;

/**
 * Main entry point for the Judgment SDK. Provides access to tracer, scorer, and
 * evaluation factories.
 */
public final class JudgmentClient {
    private final String             apiKey;
    private final String             organizationId;
    private final String             apiUrl;
    private final JudgmentSyncClient internalClient;

    private JudgmentClient(Builder builder) {
        this.apiKey = Objects.requireNonNull(builder.apiKey, "apiKey required");
        this.organizationId = Objects.requireNonNull(builder.organizationId, "organizationId required");
        this.apiUrl = builder.apiUrl != null ? builder.apiUrl : Env.JUDGMENT_API_URL;
        this.internalClient = new JudgmentSyncClient(apiUrl, apiKey, organizationId);
    }

    /**
     * Returns a factory for creating tracers.
     *
     * @return the tracer factory
     */
    public TracerFactory tracer() {
        return new TracerFactory(internalClient);
    }

    /**
     * Returns a factory for creating scorers.
     *
     * @return the scorer factory
     */
    public ScorersFactory scorers() {
        return new ScorersFactory(internalClient);
    }

    /**
     * Returns a factory for creating evaluations.
     *
     * @return the evaluation factory
     */
    public EvaluationFactory evaluation() {
        return new EvaluationFactory(internalClient);
    }

    /**
     * Creates a new builder for configuring a JudgmentClient.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring and creating JudgmentClient instances.
     */
    public static final class Builder {
        private String apiKey         = Env.JUDGMENT_API_KEY;
        private String organizationId = Env.JUDGMENT_ORG_ID;
        private String apiUrl         = Env.JUDGMENT_API_URL;

        /**
         * Sets the API key for authentication.
         *
         * @param apiKey
         *            the API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the organization ID.
         *
         * @param organizationId
         *            the organization ID
         * @return this builder
         */
        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        /**
         * Sets the API URL.
         *
         * @param apiUrl
         *            the API URL
         * @return this builder
         */
        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        /**
         * Builds and returns a new JudgmentClient instance.
         *
         * @return the configured JudgmentClient
         */
        public JudgmentClient build() {
            return new JudgmentClient(this);
        }
    }
}
