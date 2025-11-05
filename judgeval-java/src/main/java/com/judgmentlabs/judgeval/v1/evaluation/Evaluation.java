package com.judgmentlabs.judgeval.v1.evaluation;

import java.util.Objects;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

public final class Evaluation {
    private final JudgmentSyncClient client;

    private Evaluation(Builder builder) {
        this.client = Objects.requireNonNull(builder.client, "client required");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private JudgmentSyncClient client;

        Builder client(JudgmentSyncClient client) {
            this.client = client;
            return this;
        }

        public Evaluation build() {
            return new Evaluation(this);
        }
    }
}
