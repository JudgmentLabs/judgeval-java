package com.judgmentlabs.judgeval.v1;

import java.util.Objects;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.v1.evaluation.EvaluationFactory;
import com.judgmentlabs.judgeval.v1.scorers.ScorersFactory;
import com.judgmentlabs.judgeval.v1.tracer.TracerFactory;

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

    public TracerFactory tracer() {
        return new TracerFactory(internalClient, apiKey, organizationId, apiUrl);
    }

    public ScorersFactory scorers() {
        return new ScorersFactory(internalClient, apiKey, organizationId);
    }

    public EvaluationFactory evaluation() {
        return new EvaluationFactory(internalClient, apiKey, organizationId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String apiKey         = Env.JUDGMENT_API_KEY;
        private String organizationId = Env.JUDGMENT_ORG_ID;
        private String apiUrl         = Env.JUDGMENT_API_URL;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        public JudgmentClient build() {
            return new JudgmentClient(this);
        }
    }
}
