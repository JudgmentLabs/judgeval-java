package com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer;

import java.util.HashMap;
import java.util.Map;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.exceptions.JudgmentAPIError;
import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;

public class TracePromptScorer extends BasePromptScorer {

    public TracePromptScorer(
            String name, String prompt, double threshold, Map<String, Double> options) {
        super(
                APIScorerType.TRACE_PROMPT_SCORER,
                name,
                prompt,
                threshold,
                options,
                Env.JUDGMENT_API_KEY,
                Env.JUDGMENT_ORG_ID);
    }

    public TracePromptScorer(
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options,
            String judgmentApiKey,
            String organizationId) {
        super(
                APIScorerType.TRACE_PROMPT_SCORER,
                name,
                prompt,
                threshold,
                options,
                judgmentApiKey,
                organizationId);
    }

    public static TracePromptScorer get(String name) {
        return get(name, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
    }

    public static TracePromptScorer get(String name, String judgmentApiKey, String organizationId) {
        com.judgmentlabs.judgeval.internal.api.models.PromptScorer scorerConfig =
                fetchPromptScorer(name, judgmentApiKey, organizationId);

        if (!Boolean.TRUE.equals(scorerConfig.getIsTrace())) {
            throw new JudgmentAPIError(
                    400, "Scorer with name " + name + " is not a TracePromptScorer");
        }

        Map<String, Double> options = null;
        if (scorerConfig.getOptions() != null) {
            if (scorerConfig.getOptions() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> rawOptions = (Map<String, Object>) scorerConfig.getOptions();
                options = new HashMap<>();
                for (Map.Entry<String, Object> entry : rawOptions.entrySet()) {
                    if (entry.getValue() instanceof Number) {
                        options.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
                    }
                }
            }
        }

        return new TracePromptScorer(
                name,
                scorerConfig.getPrompt(),
                scorerConfig.getThreshold() != null ? scorerConfig.getThreshold() : 0.5,
                options,
                judgmentApiKey,
                organizationId);
    }

    public static TracePromptScorer create(String name, String prompt) {
        return create(name, prompt, 0.5, null);
    }

    public static TracePromptScorer create(String name, String prompt, double threshold) {
        return create(name, prompt, threshold, null);
    }

    public static TracePromptScorer create(
            String name, String prompt, double threshold, Map<String, Double> options) {
        return create(name, prompt, threshold, options, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
    }

    public static TracePromptScorer create(
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options,
            String judgmentApiKey,
            String organizationId) {
        if (!scorerExists(name, judgmentApiKey, organizationId)) {
            pushPromptScorer(
                    name, prompt, threshold, options, judgmentApiKey, organizationId, true);
            return new TracePromptScorer(
                    name, prompt, threshold, options, judgmentApiKey, organizationId);
        } else {
            throw new JudgmentAPIError(
                    400,
                    "Scorer with name "
                            + name
                            + " already exists. Either use the existing scorer with the get() method or use a new name.");
        }
    }

    @Override
    public ScorerConfig getScorerConfig() {
        ScorerConfig cfg = new ScorerConfig();
        cfg.setScoreType(getScoreType());
        cfg.setThreshold(getThreshold());
        cfg.setName(getName());
        cfg.setStrictMode(getStrictMode());
        cfg.setRequiredParams(getRequiredParams());
        Map<String, Object> kwargs = new HashMap<>();
        kwargs.put("prompt", prompt);
        if (options != null) {
            kwargs.put("options", options);
        }
        if (getAdditionalProperties() != null) {
            kwargs.putAll(getAdditionalProperties());
        }
        cfg.setKwargs(kwargs);
        return cfg;
    }

    @Override
    protected boolean isTrace() {
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String name, String prompt) {
        return new Builder(name, prompt);
    }

    public static final class Builder {
        private String name;
        private String prompt;
        private double threshold = 0.5;
        private Map<String, Double> options;
        private String judgmentApiKey = Env.JUDGMENT_API_KEY;
        private String organizationId = Env.JUDGMENT_ORG_ID;

        private Builder() {}

        private Builder(String name, String prompt) {
            this.name = name;
            this.prompt = prompt;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder threshold(double threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder options(Map<String, Double> options) {
            this.options = options;
            return this;
        }

        public Builder option(String key, Double value) {
            if (this.options == null) {
                this.options = new HashMap<>();
            }
            this.options.put(key, value);
            return this;
        }

        public Builder judgmentApiKey(String judgmentApiKey) {
            this.judgmentApiKey = judgmentApiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public TracePromptScorer build() {
            if (name == null || prompt == null) {
                throw new IllegalArgumentException("Name and prompt are required");
            }
            return new TracePromptScorer(
                    name, prompt, threshold, options, judgmentApiKey, organizationId);
        }
    }
}
