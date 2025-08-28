package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.api.models.FetchPromptScorerRequest;
import com.judgmentlabs.judgeval.api.models.FetchPromptScorerResponse;
import com.judgmentlabs.judgeval.api.models.SavePromptScorerRequest;
import com.judgmentlabs.judgeval.api.models.SavePromptScorerResponse;
import com.judgmentlabs.judgeval.api.models.ScorerConfig;
import com.judgmentlabs.judgeval.api.models.ScorerExistsRequest;
import com.judgmentlabs.judgeval.api.models.ScorerExistsResponse;
import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.exceptions.JudgmentAPIError;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class PromptScorer extends APIScorer {
    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("options")
    private Map<String, Double> options;

    @JsonIgnore
    private JudgmentSyncClient client;

    public PromptScorer(String name, String prompt, double threshold, Map<String, Double> options) {
        this(
                new JudgmentSyncClient(
                        Env.JUDGMENT_API_URL, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID),
                name,
                prompt,
                threshold,
                options);
    }

    public PromptScorer(
            JudgmentSyncClient client,
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options) {
        super(APIScorerType.PROMPT_SCORER);
        this.client = client;
        setName(name);
        this.prompt = prompt;
        this.options = options;
        setThreshold(threshold);
    }

    @Override
    public Object toTransport() {
        ScorerConfig cfg = new ScorerConfig();
        cfg.setScoreType(getScoreType());
        cfg.setThreshold(getThreshold());
        cfg.setName(getName());
        cfg.setStrictMode(isStrictMode());
        cfg.setRequiredParams(getRequiredParams());
        Map<String, Object> kwargs = new HashMap<>();
        kwargs.put("prompt", prompt);
        if (options != null)
            kwargs.put("options", options);
        if (getAdditionalProperties() != null)
            kwargs.putAll(getAdditionalProperties());
        cfg.setKwargs(kwargs);
        return cfg;
    }

    public static PromptScorer get(String name) {
        return get(name, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
    }

    public static PromptScorer get(String name, String judgmentApiKey, String organizationId) {
        try {
            JudgmentSyncClient client = new JudgmentSyncClient(Env.JUDGMENT_API_URL, judgmentApiKey, organizationId);
            FetchPromptScorerRequest request = new FetchPromptScorerRequest();
            request.setName(name);

            FetchPromptScorerResponse response = client.fetchScorer(request);
            com.judgmentlabs.judgeval.api.models.PromptScorer scorerConfig = response.getScorer();

            return new PromptScorer(
                    client,
                    name,
                    scorerConfig.getPrompt(),
                    scorerConfig.getThreshold(),
                    (Map<String, Double>) scorerConfig.getOptions());
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(500, "Failed to fetch prompt scorer: " + e.getMessage());
        }
    }

    public static PromptScorer create(String name, String prompt) {
        return create(name, prompt, 0.5, null);
    }

    public static PromptScorer create(String name, String prompt, double threshold) {
        return create(name, prompt, threshold, null);
    }

    public static PromptScorer create(
            String name, String prompt, double threshold, Map<String, Double> options) {
        return create(name, prompt, threshold, options, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
    }

    public static PromptScorer create(
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options,
            String judgmentApiKey,
            String organizationId) {
        try {
            JudgmentSyncClient client = new JudgmentSyncClient(Env.JUDGMENT_API_URL, judgmentApiKey, organizationId);

            ScorerExistsRequest existsRequest = new ScorerExistsRequest();
            existsRequest.setName(name);

            ScorerExistsResponse existsResponse = client.scorerExists(existsRequest);
            boolean exists = Boolean.TRUE.equals(existsResponse.getExists());

            if (exists) {
                throw new JudgmentAPIError(
                        400,
                        "Scorer with name "
                                + name
                                + " already exists. Either use the existing scorer with the get() method or use a new name.");
            }

            pushPromptScorer(client, name, prompt, threshold, options);
            return new PromptScorer(client, name, prompt, threshold, options);
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(500, "Failed to create prompt scorer: " + e.getMessage());
        }
    }

    public void setThreshold(double threshold) {
        super.setThreshold(threshold);
        pushPromptScorer();
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
        pushPromptScorer();
    }

    public void setOptions(Map<String, Double> options) {
        this.options = options;
        pushPromptScorer();
    }

    public void appendToPrompt(String promptAddition) {
        this.prompt += promptAddition;
        pushPromptScorer();
    }

    public Double getThreshold() {
        return super.getThreshold();
    }

    public String getPrompt() {
        return prompt;
    }

    public Map<String, Double> getOptions() {
        return options != null ? new HashMap<>(options) : null;
    }

    public String getScorerName() {
        return getName();
    }

    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("name", getName());
        config.put("prompt", prompt);
        config.put("threshold", getThreshold());
        config.put("options", options);
        return config;
    }

    private void pushPromptScorer() {
        pushPromptScorer(this.client, getName(), prompt, getThreshold(), options);
    }

    private static String pushPromptScorer(
            JudgmentSyncClient client,
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options) {
        try {
            SavePromptScorerRequest request = new SavePromptScorerRequest();
            request.setName(name);
            request.setPrompt(prompt);
            request.setThreshold(threshold);
            request.setOptions(options);
            request.setIsTrace(false);

            SavePromptScorerResponse response = client.saveScorer(request);
            return response != null ? response.getName() : null;
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(500, "Failed to save prompt scorer: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "PromptScorer(name="
                + getName()
                + ", prompt="
                + prompt
                + ", threshold="
                + getThreshold()
                + ", options="
                + options
                + ")";
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
        private JudgmentSyncClient client;

        private Builder() {
        }

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

        public Builder client(JudgmentSyncClient client) {
            this.client = client;
            return this;
        }

        public PromptScorer build() {
            if (name == null || prompt == null) {
                throw new IllegalArgumentException("Name and prompt are required");
            }

            if (client != null) {
                return new PromptScorer(client, name, prompt, threshold, options);
            } else {
                return new PromptScorer(name, prompt, threshold, options);
            }
        }
    }
}
