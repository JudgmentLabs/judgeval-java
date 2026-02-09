package com.judgmentlabs.judgeval.scorers;

import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.scorers.built_in.BuiltInScorersFactory;
import com.judgmentlabs.judgeval.scorers.custom_scorer.CustomScorerFactory;
import com.judgmentlabs.judgeval.scorers.prompt_scorer.PromptScorerFactory;

public final class ScorersFactory {
    private final JudgmentSyncClient client;
    private final Optional<String>   projectId;

    public ScorersFactory(JudgmentSyncClient client, Optional<String> projectId) {
        this.client = client;
        this.projectId = projectId;
    }

    public PromptScorerFactory promptScorer() {
        return new PromptScorerFactory(client, projectId.orElse(""), false);
    }

    public PromptScorerFactory tracePromptScorer() {
        return new PromptScorerFactory(client, projectId.orElse(""), true);
    }

    public CustomScorerFactory customScorer() {
        return new CustomScorerFactory(projectId);
    }

    public BuiltInScorersFactory builtIn() {
        return new BuiltInScorersFactory();
    }
}
