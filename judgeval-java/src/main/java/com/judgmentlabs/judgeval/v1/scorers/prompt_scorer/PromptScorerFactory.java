package com.judgmentlabs.judgeval.v1.scorers.prompt_scorer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.judgmentlabs.judgeval.exceptions.JudgmentAPIError;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersRequest;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersResponse;

public final class PromptScorerFactory {
    private final JudgmentSyncClient                                                               client;
    private final String                                                                           apiKey;
    private final String                                                                           organizationId;
    private final boolean                                                                          isTrace;
    private static final Map<CacheKey, com.judgmentlabs.judgeval.internal.api.models.PromptScorer> cache = new ConcurrentHashMap<>();

    public PromptScorerFactory(JudgmentSyncClient client, String apiKey, String organizationId, boolean isTrace) {
        this.client = client;
        this.apiKey = apiKey;
        this.organizationId = organizationId;
        this.isTrace = isTrace;
    }

    public PromptScorer get(String name) {
        CacheKey key = new CacheKey(name, apiKey, organizationId);
        com.judgmentlabs.judgeval.internal.api.models.PromptScorer cached = cache.get(key);
        if (cached != null) {
            return createFromModel(cached, name);
        }

        try {
            FetchPromptScorersRequest request = new FetchPromptScorersRequest();
            request.setNames(java.util.Collections.singletonList(name));

            FetchPromptScorersResponse response = client.fetchScorers(request);

            com.judgmentlabs.judgeval.internal.api.models.PromptScorer scorer = Optional.ofNullable(response)
                    .map(FetchPromptScorersResponse::getScorers)
                    .filter(scorers -> scorers != null && !scorers.isEmpty())
                    .map(scorers -> scorers.get(0))
                    .orElseThrow(
                            () -> new JudgmentAPIError(404, "Failed to fetch prompt scorer '" + name + "': not found"));

            if (Boolean.TRUE.equals(scorer.getIsTrace()) != isTrace) {
                String expectedType = isTrace ? "TracePromptScorer" : "PromptScorer";
                String actualType = Boolean.TRUE.equals(scorer.getIsTrace()) ? "TracePromptScorer" : "PromptScorer";
                throw new JudgmentAPIError(400,
                        "Scorer with name " + name + " is a " + actualType + ", not a " + expectedType);
            }

            cache.put(key, scorer);
            return createFromModel(scorer, name);
        } catch (JudgmentAPIError e) {
            throw e;
        } catch (Exception e) {
            throw new JudgmentAPIError(500, "Failed to fetch prompt scorer '" + name + "': " + e.getMessage());
        }
    }

    private PromptScorer createFromModel(com.judgmentlabs.judgeval.internal.api.models.PromptScorer model,
            String name) {
        Map<String, Double> options = null;
        if (model.getOptions() != null) {
            if (model.getOptions() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> rawOptions = (Map<String, Object>) model.getOptions();
                options = new HashMap<>();
                for (Map.Entry<String, Object> entry : rawOptions.entrySet()) {
                    if (entry.getValue() instanceof Number) {
                        options.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
                    }
                }
            }
        }

        return PromptScorer.builder()
                .name(name)
                .prompt(model.getPrompt())
                .threshold(Optional.ofNullable(model.getThreshold()).orElse(0.5))
                .options(options)
                .apiKey(apiKey)
                .organizationId(organizationId)
                .isTrace(isTrace)
                .build();
    }

    public PromptScorer.Builder create() {
        return PromptScorer.builder()
                .apiKey(apiKey)
                .organizationId(organizationId)
                .isTrace(isTrace);
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
