package com.judgmentlabs.judgeval.scorers;

import java.util.Optional;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.scorers.built_in.BuiltInScorersFactory;
import com.judgmentlabs.judgeval.scorers.custom_scorer.CustomScorerFactory;
import com.judgmentlabs.judgeval.scorers.prompt_scorer.PromptScorerFactory;

/**
 * Factory for creating scorer builders and accessing scorer types.
 */
public final class ScorersFactory {
    private final JudgmentSyncClient client;
    private final Optional<String>   projectId;

    public ScorersFactory(JudgmentSyncClient client, Optional<String> projectId) {
        this.client = client;
        this.projectId = projectId;
    }

    /**
     * Returns a factory for creating prompt-based scorers.
     *
     * @return the prompt scorer factory
     */
    public PromptScorerFactory promptScorer() {
        return new PromptScorerFactory(client, projectId, false);
    }

    /**
     * Returns a factory for creating trace-level prompt scorers.
     *
     * @return the trace prompt scorer factory
     */
    public PromptScorerFactory tracePromptScorer() {
        return new PromptScorerFactory(client, projectId, true);
    }

    /**
     * Returns a factory for creating custom scorers.
     *
     * @return the custom scorer factory
     */
    public CustomScorerFactory customScorer() {
        return new CustomScorerFactory(projectId);
    }

    /**
     * Returns a factory for creating built-in scorers.
     *
     * @return the built-in scorers factory
     */
    public BuiltInScorersFactory builtIn() {
        return new BuiltInScorersFactory();
    }
}
