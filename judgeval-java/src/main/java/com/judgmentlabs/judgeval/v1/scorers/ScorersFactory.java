package com.judgmentlabs.judgeval.v1.scorers;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.BuiltInScorersFactory;
import com.judgmentlabs.judgeval.v1.scorers.custom_scorer.CustomScorerFactory;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.PromptScorerFactory;

public final class ScorersFactory {
    private final JudgmentSyncClient client;
    private final String             apiKey;
    private final String             organizationId;

    public ScorersFactory(JudgmentSyncClient client, String apiKey, String organizationId) {
        this.client = client;
        this.apiKey = apiKey;
        this.organizationId = organizationId;
    }

    public PromptScorerFactory promptScorer() {
        return new PromptScorerFactory(client, apiKey, organizationId, false);
    }

    public PromptScorerFactory tracePromptScorer() {
        return new PromptScorerFactory(client, apiKey, organizationId, true);
    }

    public CustomScorerFactory customScorer() {
        return new CustomScorerFactory();
    }

    public BuiltInScorersFactory builtIn() {
        return new BuiltInScorersFactory();
    }
}
