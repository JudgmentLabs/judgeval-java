package com.judgmentlabs.judgeval.scorers.prompt_scorer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersResponse;
import com.judgmentlabs.judgeval.utils.Guards;
import com.judgmentlabs.judgeval.utils.Logger;

/**
 * Factory for retrieving prompt-based scorers.
 */
public final class PromptScorerFactory {
    private final JudgmentSyncClient                                                               client;
    private final Optional<String>                                                                 projectId;
    private final boolean                                                                          isTrace;
    private static final Map<CacheKey, com.judgmentlabs.judgeval.internal.api.models.PromptScorer> cache = new ConcurrentHashMap<>();

    public PromptScorerFactory(JudgmentSyncClient client, Optional<String> projectId, boolean isTrace) {
        this.client = client;
        this.projectId = projectId;
        this.isTrace = isTrace;
    }

    /**
     * Retrieves a prompt scorer by name from the Judgment API.
     * Results are cached to avoid repeated API calls.
     *
     * @param name
     *            the scorer name
     * @return the configured prompt scorer or null if not found or retrieval fails
     */
    public PromptScorer get(String name) {
        return Guards.expectProjectId(projectId)
                .map(pid -> fetchAndCache(name, pid))
                .orElse(null);
    }

    private PromptScorer fetchAndCache(String name, String pid) {
        CacheKey key = new CacheKey(name, client.getApiKey(), client.getOrganizationId());
        com.judgmentlabs.judgeval.internal.api.models.PromptScorer cached = cache.get(key);
        if (cached != null) {
            return createFromModel(cached, name);
        }

        try {
            FetchPromptScorersResponse response = client.getProjectsScorers(pid, name, String.valueOf(isTrace));

            return Optional.ofNullable(response)
                    .map(FetchPromptScorersResponse::getScorers)
                    .filter(scorers -> scorers != null && !scorers.isEmpty())
                    .map(scorers -> scorers.get(0))
                    .filter(scorer -> {
                        if (Boolean.TRUE.equals(scorer.getIsTrace()) != isTrace) {
                            Logger.error("Scorer '" + name + "' is a "
                                    + (Boolean.TRUE.equals(scorer.getIsTrace()) ? "TracePromptScorer" : "PromptScorer")
                                    + ", not a " + (isTrace ? "TracePromptScorer" : "PromptScorer"));
                            return false;
                        }
                        return true;
                    })
                    .map(scorer -> {
                        cache.put(key, scorer);
                        return createFromModel(scorer, name);
                    })
                    .orElseGet(() -> {
                        Logger.error("Failed to fetch prompt scorer '" + name + "': not found");
                        return null;
                    });
        } catch (Exception e) {
            Logger.error("Failed to fetch prompt scorer '" + name + "': " + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private PromptScorer createFromModel(com.judgmentlabs.judgeval.internal.api.models.PromptScorer model,
            String name) {
        Map<String, Double> options = Optional.ofNullable(model.getOptions())
                .filter(Map.class::isInstance)
                .map(o -> (Map<String, Object>) o)
                .map(raw -> {
                    Map<String, Double> result = new HashMap<>();
                    raw.forEach((k, v) -> {
                        if (v instanceof Number) result.put(k, ((Number) v).doubleValue());
                    });
                    return result;
                })
                .orElse(null);

        return PromptScorer.builder()
                .name(name)
                .prompt(model.getPrompt())
                .threshold(Optional.ofNullable(model.getThreshold()).orElse(0.5))
                .options(options)
                .isTrace(isTrace)
                .build();
    }

    private static final class CacheKey {
        private final String name;
        private final String apiKey;
        private final String organizationId;

        CacheKey(String name, String apiKey, String organizationId) {
            this.name = name;
            this.apiKey = apiKey;
            this.organizationId = organizationId;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            CacheKey that = (CacheKey) obj;
            return Objects.equals(name, that.name) && Objects.equals(apiKey, that.apiKey)
                    && Objects.equals(organizationId, that.organizationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, apiKey, organizationId);
        }
    }
}
